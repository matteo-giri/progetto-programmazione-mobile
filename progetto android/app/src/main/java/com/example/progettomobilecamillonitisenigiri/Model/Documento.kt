package com.example.progettomobilecamillonitisenigiri.Model
/*
* Modello del documento
* */
class Documento(titolo: String, id: String, url: String) {
      var titolo: String
      var id: String
      var url: String
    init {
        this.id=id
        this.titolo=titolo
        this.url=url
    }
    constructor() : this("", "", "") {}
}