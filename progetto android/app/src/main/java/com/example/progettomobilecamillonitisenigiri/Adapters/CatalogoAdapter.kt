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
import com.example.progettomobilecamillonitisenigiri.Model.CategoriaListModel
import com.example.progettomobilecamillonitisenigiri.R
/*
* Adapter del catalogo
* */

class CatalogoAdapter(val data: List<CategoriaListModel>, val context:Context?,val monPopularAdapter: CorsoAdapter.OnCorsoListener)
    : RecyclerView.Adapter<CatalogoAdapter.CatalogoAdapterViewHolder>() {
    class CatalogoAdapterViewHolder(val recyclerView: View,val onCorsoListener: CorsoAdapter.OnCorsoListener) : RecyclerView.ViewHolder(recyclerView){
        val CorsiCat = recyclerView.findViewById<ConstraintLayout>(R.id.containerTopCat)
    }
    //per creare un layout di item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CatalogoAdapterViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_top_in_cat,
            parent, false
        )
        return CatalogoAdapterViewHolder(layout,monPopularAdapter)
    }

    //quando si ricicla un layout di item aggiornando i dati mostrati all’interno
    override fun onBindViewHolder(holder: CatalogoAdapterViewHolder, position: Int) {
        holder.CorsiCat.findViewById<TextView>(R.id.cat).text = data.get(position).categoria
        holder.CorsiCat.findViewById<RecyclerView>(R.id.recyclerViewCat).layoutManager =  LinearLayoutManager(context,LinearLayoutManager.HORIZONTAL, false)
        holder.CorsiCat.findViewById<RecyclerView>(R.id.recyclerViewCat).adapter = CorsoAdapter(data.get(position).corsi,monPopularAdapter)
    }

    //numero totale di item nella lista
    override fun getItemCount(): Int = data.size

}