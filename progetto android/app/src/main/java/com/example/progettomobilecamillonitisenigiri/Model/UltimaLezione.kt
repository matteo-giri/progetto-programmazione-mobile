package com.example.progettomobilecamillonitisenigiri.Model
/*
* Modello dell'ultimaLezione riprodotta
* */
class UltimaLezione(
     id_corso: String ,
     url_lezione: String? ,
     id_lezione: String ,
     secondi: Float
){
    var secondi: Float
    var url_lezione: String?
    var id_corso: String
    var id_lezione: String

    init {
    this.id_corso=id_corso
    this.url_lezione=url_lezione
    this.secondi=secondi
    this.id_lezione=id_lezione

}
constructor() : this( "","", "", 0.toFloat()) {}
}