package com.example.progettomobilecamillonitisenigiri.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettomobilecamillonitisenigiri.Corso.FragmentForumCorso
import com.example.progettomobilecamillonitisenigiri.Model.DomandaForum
import com.example.progettomobilecamillonitisenigiri.Model.RispostaForum

import com.example.progettomobilecamillonitisenigiri.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
/*
* Adapter del Delle Domande del Forum
* */

class DomandeForumAdapter(val data: List<DomandaForum>, val context:Context?, val monDomandeAdapterListener:OnDomandeAdapterListener  , val fragment:FragmentForumCorso): RecyclerView.Adapter<DomandeForumAdapter.DomandeForumViewHolder>(){
    class DomandeForumViewHolder(val box: View, val onDomandeAdapterListener: OnDomandeAdapterListener): RecyclerView.ViewHolder(box), View.OnClickListener{
        init{
            box.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            onDomandeAdapterListener.onDomandeClick(adapterPosition, v)
        }
        val domanda = box.findViewById<CardView>(R.id.cardViewDomande)
    }

    //per creare un layout di item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DomandeForumViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_domanda,
            parent, false
        )
        return DomandeForumViewHolder(layout,monDomandeAdapterListener)
    }

    //quando si ricicla un layout di item aggiornando i dati mostrati all’interno
    override fun onBindViewHolder(holder: DomandeForumViewHolder, position: Int) {
        holder.domanda.findViewById<TextView>(R.id.idDomanda).text = data.get(position).id.toString()
        holder.domanda.findViewById<TextView>(R.id.nome).text = data.get(position).nomeUtente
        holder.domanda.findViewById<TextView>(R.id.cognome).text = data.get(position).cognomeUtente
        holder.domanda.findViewById<TextView>(R.id.domanda).text = data.get(position).domanda
        val inputRisposta = holder.domanda.findViewById<TextInputEditText>(R.id.aggiungiRisposta)
        holder.domanda.findViewById<TextInputLayout>(R.id.aggiungiRispostaContainer)?.setEndIconOnClickListener {
                if(!inputRisposta!!.text.toString().equals(null)){
                        fragment.addRispostaFrag(inputRisposta!!.text.toString(),data.get(position).id)
                }
            }
        holder.domanda.findViewById<RecyclerView>(R.id.list_risposte).layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.domanda.findViewById<RecyclerView>(R.id.list_risposte).adapter = RispostaForumAdapter(data.get(position).risposte)

    }

    //numero totale di item nella lista
    override fun getItemCount(): Int = data.size

    //interfaccia per poter definire il metodo onclick da altre parti
    interface OnDomandeAdapterListener{
        fun onDomandeClick(position: Int, v: View?)
    }
}