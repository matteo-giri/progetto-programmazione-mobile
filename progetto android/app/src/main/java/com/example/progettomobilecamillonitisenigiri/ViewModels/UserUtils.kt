package com.example.progettomobilecamillonitisenigiri.ViewModels

import com.example.progettomobilecamillonitisenigiri.Model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
/*
UserUtils
* Utils per elaborare lo snapshot passato alla funzione readData  per elaborare l'utente corrente
* */

class UserUtils {
    //istanza della FirebaseAuth
    private var loggedUser = FirebaseAuth.getInstance().currentUser
    private lateinit var utenteLoggato: User

    init {

    }

    //funzione che elabora e mappa l'utente dallo snapshot
    fun readData(snapshot: DataSnapshot) {

        if (snapshot.child("Users").child(loggedUser!!.uid)!!.exists()) {
            val utenteSnap = snapshot.child("Users").child(loggedUser!!.uid)
            utenteLoggato = utenteSnap.getValue(User::class.java)!!

        }

    }


    //getters e setters

    fun getUtente(): User {
        return utenteLoggato
    }

    fun getCategoriePreferite(): List<String> {
        return utenteLoggato.categoriePref
    }

    fun getIscrizioni(): List<String> {
        return utenteLoggato.iscrizioni
    }

    //Aggiunge/Elimina iscrizione aggiornando intera lista iscrizioni
    fun setIscrizione(iscrizione_id: String) {
        if(utenteLoggato.iscrizioni.contains(iscrizione_id))
            utenteLoggato.iscrizioni.remove(iscrizione_id)
        else
            utenteLoggato.iscrizioni.add(iscrizione_id)
    }

    fun getWishlist(): List<String> {
        return utenteLoggato.wishlist
    }

    //Aggiunge/Elimina iscrizione aggiornando intera lista iscrizioni
    fun setWishlist(iscrizione_id: String) {
        if(utenteLoggato.wishlist.contains(iscrizione_id))
            utenteLoggato.wishlist.remove(iscrizione_id)
        else
            utenteLoggato.wishlist.add(iscrizione_id)
    }

}