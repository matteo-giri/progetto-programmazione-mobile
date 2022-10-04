package com.example.progettomobilecamillonitisenigiri.ViewModels

import androidx.lifecycle.*
import com.example.progettomobilecamillonitisenigiri.Model.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
/*
* View model che si occupa della gestione al Database remoto di firebase
* e gestisce liste di liveData rimangono aggiornate dinamicamente assieme al db
* */
class FirebaseConnection : ViewModel() {

    //Reference FirebaseDB
    private var database: DatabaseReference
    private var corsoDatabaseReference: DatabaseReference
    private var userDatabaseReference: DatabaseReference
    //Istanza utente loggato
    private var loggedUser = FirebaseAuth.getInstance().currentUser

    // Mutable Live Data
    private val listCorsi = MutableLiveData<List<Corso>>()
    private val listAggiuntiDiRecente = MutableLiveData<List<Corso>>()
    private val listCategorie = MutableLiveData<Set<String>>()
    private val listLezioni = MutableLiveData<HashMap<String, ArrayList<Lezione>>>()
    private val listDispense = MutableLiveData<HashMap<String, ArrayList<Documento>>>()
    private val listCorsiPerCat = MutableLiveData<HashMap<String, ArrayList<Corso>>>()
    private val listDomande = MutableLiveData<HashMap<String, ArrayList<DomandaForum>>>()


    // Live data relativi tabella utente
    private val categoriePreferite = MutableLiveData<List<String>>()
    private val currentUser = MutableLiveData<User>()

    //Istanziazione delle utils utilizzate per "iterare" lo snapshot passato
    private val corsoUtils: CorsoUtils = CorsoUtils()
    private val utenteUtils: UserUtils = UserUtils()

    init {
        //Istanziazione delle reference al real time database
        database = FirebaseDatabase.getInstance().reference
        userDatabaseReference = database!!.child("Users")
        corsoDatabaseReference = database!!.child("Corsi")

        readData() //funzione che "ascolta" in modo asincrono il db
        readDataOnce() //funzione che "ascolta" in modo asincrono il db, una sola volta

    }

    //funzione che "ascolta" in modo asincrono il db
    fun readData() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //Chiamata a utility utente e popolazione livedata che avviene ad ogni cambio nel DB
                utenteUtils.readData(snapshot)
                currentUser.postValue(utenteUtils.getUtente())
                categoriePreferite.postValue(utenteUtils.getCategoriePreferite())

                //Chiamata a utility per popolazione lista corsi che avviene ad ogni cambio nel DB
                listDomande.postValue(corsoUtils.getDomande())
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    //funzione che "ascolta" in modo asincrono il db, una sola volta
    fun readDataOnce() {
        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                //popolazione componenti accessorie ai corsi che avviene solo una volta al lancio dell'activity
                corsoUtils.readData(snapshot)
                listCorsi.postValue(corsoUtils.getCorsi())
                listAggiuntiDiRecente.postValue(
                    corsoUtils.getCorsi().reversed()
                )

                listCorsiPerCat.postValue(corsoUtils.getCorsiPerCat())
                listCategorie.postValue(corsoUtils.getCat())
                listDispense.postValue(corsoUtils.getDispense())
                listLezioni.postValue(corsoUtils.getLezioni())

            }
            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

                            /******************** Funzioni che settano/aggiornano Dati*************************/

    //questa funzione aggiunge/elimina il corso dalle iscrizioni dell'utente richiamando la setiscrizione dell'utenteUtils
    fun iscriviti(id_corso: String) {
        utenteUtils.setIscrizione(id_corso)
        //aggiorna nel db l'iscrizione
        loggedUser.let { it1 ->
            userDatabaseReference!!.child(it1!!.uid).setValue(currentUser.value as User)
        }
    }

    //questa funzione aggiunge/elimina il corso dalla wishlist dell'utente richiamando la setWishlist dell'utenteUtils
    fun wishlist(id_corso: String) {
        utenteUtils.setWishlist(id_corso)
        //aggiorna nel db la wishlist
        loggedUser.let { it1 ->
            userDatabaseReference!!.child(it1!!.uid).setValue(currentUser.value as User)
        }
    }

    //funzione che aggiorna l'utente passando un oggetto User
    fun setUtente(utente: User) {
        loggedUser.let { it1 ->
            userDatabaseReference!!.child(it1!!.uid).setValue(utente)
        }
    }

    //le recensioni vengono aggiunte a una lista di recensioni nella tabella del corso perchè altrimenti per estrapolare la media bisognava tirar fuori le recensioni da ogni singolo utente
    fun setRecensione(id_corso: String, recensione: Float) {
        loggedUser.let { it1 ->
            //Se nel corso ricercato tramite id_corso in listCorsi, c'è già una recensione dell'utente con l'uid corrente, aggiorna la recensione, altrimenti la aggiunge alla lista delle recensioni
            if (listCorsi.value?.get(id_corso?.toInt())?.recensioni?.contains(it1!!.uid) == true)
                listCorsi.value?.get(id_corso?.toInt())?.recensioni?.replace(it1!!.uid, recensione)
            else listCorsi.value?.get(id_corso.toInt())?.recensioni?.put(it1!!.uid, recensione)

            //aggiorna la lista delle recensioni relative al corso con id_corso nel db
            database.child("Corsi").child(id_corso).child("recensioni")
                .setValue(listCorsi.value?.get(id_corso.toInt())?.recensioni)
        }

    }

   // Funzione che aggiunge domanda al forum e ritorna uno stato di avvenuta aggiunta
    fun addDomanda(domanda: DomandaForum, id_corso: String): Boolean {
        var aggiunta = false
        val listaDomandeCorso = listDomande.value?.get(id_corso)
        if (!listaDomandeCorso!!.contains(domanda)) {
            listaDomandeCorso.add(domanda)
            aggiunta = true
        }
       //aggiornamento del db
        corsoDatabaseReference.child(id_corso).child("forum").setValue(listaDomandeCorso)
        return aggiunta
    }

    // Funzione che aggiunge risposta al forum e ritorna uno stato di avvenuta aggiunta
    fun addRisposta(risposta: RispostaForum, id_corso: String, id_domanda: Int): Boolean {
        var aggiunta = false
        val risposte = listDomande.value!!.get(id_corso)!!.get(id_domanda).risposte
        if (!risposte.contains(risposta)) {
            risposte.add(risposta)
            aggiunta = true
        }
        //aggiornamento del db
        corsoDatabaseReference.child(id_corso).child("forum").child(id_domanda.toString())
            .child("risposte").setValue(risposte)
        return aggiunta
    }

    /******************** Funzioni che restituiscono Dati*************************/
    fun getUser(): MutableLiveData<User> {
        return currentUser
    }

    fun getListaCorsi(): MutableLiveData<List<Corso>> {
        return listCorsi
    }


    fun getListaLezioni(): MutableLiveData<HashMap<String, ArrayList<Lezione>>> {
        System.out.println("EntraLezione")
        return listLezioni
    }

    fun getCategorie(): MutableLiveData<Set<String>> {
        System.out.println("EntraCategoria")
        return listCategorie
    }

    fun getListaDispense(): MutableLiveData<HashMap<String, ArrayList<Documento>>> {
        System.out.println("EntraDispense")
        return listDispense
    }

    fun getCorsiPerCat(): MutableLiveData<HashMap<String, ArrayList<Corso>>> {
        return listCorsiPerCat
    }

    fun getListDomande(): MutableLiveData<HashMap<String, ArrayList<DomandaForum>>> {
        return listDomande
    }

    fun newDomandaId(id_corso: String): Int {
        val id = listDomande.value!!.get(id_corso)!!.lastIndex + 1
        return id
    }


    //Funzione che ritorna lista popolari ordinati a seconda delle recensioni
    fun getListaPopolari(corsi: ArrayList<Corso>): ArrayList<Corso> {
        corsi.sort()
        return corsi.take(5) as ArrayList<Corso>
    }

    //Funzione che ritorna lista consigliati iterando una Lista di Corsi e confrontando con categorie preferite dell'utente
    fun getListaConsigliati(corsi: ArrayList<Corso>): ArrayList<Corso> {
        val consigliati = ArrayList<Corso>()
        for (corso in corsi) {
            for (categoria in currentUser.value!!.categoriePref) {
                if (corso.categoria.equals(categoria)) {
                    consigliati.add(corso)
                }
            }

        }
        if (consigliati.isEmpty())
            return corsi.shuffled()
                .take(5) as ArrayList<Corso> //ritorna corsi se l'utente non ha categorie predefinite
        if (consigliati.size >= 5) {
            return consigliati.shuffled().take(5) as ArrayList<Corso>
        } else return consigliati.shuffled() as ArrayList<Corso>
    }

    //Funzione che ritorna corsifrequentati a seconda delle iscrizioni dell'utente
    fun getCorsiFrequentati(corsi: ArrayList<Corso>): ArrayList<Corso> {
        val frequentati = ArrayList<Corso>()
        for (corso in corsi) {
            for (iscrizioni_id in utenteUtils.getIscrizioni()) {
                if (corso.id.equals(iscrizioni_id)) {
                    frequentati.add(corso)
                }
            }

        }
        return frequentati
    }

    //Funzione che ritorna corsi dalla wishlist a seconda della wishlist dell'utente
    fun getCorsiFromWishlist(corsi: ArrayList<Corso>): ArrayList<Corso> {
        val wishlist = ArrayList<Corso>()
        for (corso in corsi) {
            for (iscrizioni_id in utenteUtils.getWishlist()) {
                if (corso.id.equals(iscrizioni_id)) {
                    wishlist.add(corso)
                }
            }

        }
        return wishlist
    }

    //Funzione che controlla se l'utente corrente è iscritto al corso passato per parametro
    fun isIscritto(idCorso: String): Boolean {
        if (currentUser.value?.iscrizioni?.contains(idCorso) == true)
            return true
        return false
    }




}

