package com.example.progettomobilecamillonitisenigiri.Model
/*
* Modello delle domande del forum
* */
class DomandaForum(val nomeUtente:String,val cognomeUtente:String,val id:Int, var domanda:String ,val risposte:ArrayList<RispostaForum>) {
    constructor() : this("Utente" ,"Utente",0, "Assente", ArrayList<RispostaForum>())

    override fun equals(other: Any?): Boolean {
        return if(other is DomandaForum) {
            (other.domanda.equals(domanda,true))
        } else false
    }
}