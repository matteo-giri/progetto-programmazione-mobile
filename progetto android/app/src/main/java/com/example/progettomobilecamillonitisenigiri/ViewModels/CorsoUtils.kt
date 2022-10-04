package com.example.progettomobilecamillonitisenigiri.ViewModels

import com.example.progettomobilecamillonitisenigiri.Model.Corso
import com.example.progettomobilecamillonitisenigiri.Model.Documento
import com.example.progettomobilecamillonitisenigiri.Model.DomandaForum
import com.example.progettomobilecamillonitisenigiri.Model.Lezione
import com.google.firebase.database.DataSnapshot
/*
CorsoUtils
* Utils per elaborare lo snapshot passato alla funzione readData e riempire le varie liste/mappe relative ai corsi
* */
class CorsoUtils {

    //Liste di appoggio per popolare  i live data nella FirebaseConnection
    private var lista_corsi = ArrayList<Corso>()
    private val lista_lezioni = HashMap<String, ArrayList<Lezione>>()
    private val lista_cat = HashSet<String>()
    private val lista_dispense = HashMap<String, ArrayList<Documento>>()
    private val lista_CorsiPerCat = HashMap<String,ArrayList<Corso>>()
    private val mapDomande = HashMap<String,ArrayList<DomandaForum>>()

    init {
        lista_corsi.clear()
        lista_cat.clear()
        lista_dispense.clear()
        lista_lezioni.clear()
        lista_CorsiPerCat.clear()
        mapDomande.clear()
    }

    //Funzione che legge i dati dello snapshot iterandoli e mappandoli in liste di oggetti
    fun readData(snapshot: DataSnapshot) {
        lista_corsi.clear()
        lista_cat.clear()
        lista_dispense.clear()
        lista_lezioni.clear()
        lista_CorsiPerCat.clear()
        mapDomande.clear()

        if (snapshot.child("Corsi")!!.exists()) {
            //itera tutti i figli dello snapshot in Corsi
            for (e in snapshot.child("Corsi").children) {

                //Aggiunta corso a lista corsi
                val corso = e.getValue(Corso::class.java)
                lista_corsi.add(corso!!)

                //Aggiungo categoria del corso a un Set
                lista_cat.add(corso.categoria)

               //Lista delle lezioni
                lista_lezioni.put(corso.id, corso.lezioni)

                //Aggiunta Documenti relative al corso
                lista_dispense.put(corso.id, corso.dispense)

                //Aggiunta Forum
                mapDomande.put(corso.id,corso.forum)
            }
        }
    }

    //getters e setters
    fun setCorsi(corsi : ArrayList<Corso>){
        lista_corsi = corsi
    }

    fun getCorsi(): ArrayList<Corso> {
        return lista_corsi
    }

    fun getLezioni(): HashMap<String, ArrayList<Lezione>> {
        return lista_lezioni
    }

    fun getDispense(): HashMap<String, ArrayList<Documento>> {
        return lista_dispense
    }

    fun getCat(): HashSet<String> {
        return lista_cat
    }
    fun getCorsiPerCat(): HashMap<String,ArrayList<Corso>>{
        for (categoria in getCat()){
            val tmp_list = ArrayList<Corso>()
            for (corso in getCorsi()) {
                if(corso.categoria.equals(categoria))
                    tmp_list.add(corso)
            }
            lista_CorsiPerCat.put(categoria,tmp_list)
        }
        return lista_CorsiPerCat
    }
    fun getDomande(): HashMap<String,ArrayList<DomandaForum>>{
        return mapDomande
    }


}