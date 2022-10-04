package com.example.progettomobilecamillonitisenigiri.Model
/*
* Modello delle lezioni
* */
class Lezione(titolo: String, descrizione: String, url: String, id: String) {
      var titolo: String
      var descrizione: String
      var url: String
      var id: String
    init {
        this.titolo=titolo
        this.descrizione=descrizione
        this.url=url
        this.id=id
    }
    constructor() : this("", "", "", "") {}
}