package com.example.progettomobilecamillonitisenigiri.Model
/*
* Modello dell'utente
* */
class User (firstName: String, lastName: String, iscrizioni: ArrayList<String>, categoriePref: ArrayList<String>,wishlist: ArrayList<String>, ultimeLezioni: ArrayList<UltimaLezione>, ultimaLezione: UltimaLezione) {
    var firstName: String
    var lastName: String
    var iscrizioni: ArrayList<String> //lista con id dei corsi a cui l'utente è iscritto
    var categoriePref: ArrayList<String> //categorie preferite dell'utente
    var wishlist: ArrayList<String> //lista con id dei corsi a cui l'utente vuole iscriversi
    var ultimeLezioni: ArrayList<UltimaLezione> //lista con le ultime lezioni viste per ogni corso
    var ultimaLezione: UltimaLezione //ultima lezione vista
    init {
        this.firstName = firstName
        this.lastName = lastName
        this.iscrizioni = iscrizioni
        this.categoriePref = categoriePref
        this.wishlist = wishlist
        this.ultimeLezioni = ultimeLezioni
        this.ultimaLezione = ultimaLezione
        if (iscrizioni == null)
            this.iscrizioni = arrayListOf()
        if (categoriePref == null)
            this.categoriePref =arrayListOf()
        if (wishlist == null)
            this.wishlist = arrayListOf()
        if (ultimeLezioni == null)
            this.ultimeLezioni = ArrayList<UltimaLezione>()

    }

    constructor() : this(
        "",
        "",
        ArrayList<String>(),
        ArrayList<String>(),
        ArrayList<String>(),
        ArrayList<UltimaLezione>(),
        UltimaLezione()
    ){}
}