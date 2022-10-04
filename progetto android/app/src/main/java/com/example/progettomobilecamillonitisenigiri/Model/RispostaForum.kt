package com.example.progettomobilecamillonitisenigiri.Model
/*
* Modello delle risposte del forum
* */
class RispostaForum(val nomeUtente:String,val cognomeUtente:String,var risposta:String) {
    constructor() : this("Utente", "Utente", "Assente")
    override fun equals(other: Any?): Boolean {
        return if(other is RispostaForum) {
            (other.risposta.equals(risposta, true))
        } else false
    }
}