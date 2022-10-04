package com.example.progettomobilecamillonitisenigiri.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.progettomobilecamillonitisenigiri.Model.Corso
import com.example.progettomobilecamillonitisenigiri.R
import com.squareup.picasso.Picasso
import java.lang.Exception

/*
* Adapter del Corso
* */

class CorsoAdapter (val data: List<Corso>, val monPopularAdapter: OnCorsoListener): RecyclerView.Adapter<CorsoAdapter.MyAdapterViewHolder>() {
    class MyAdapterViewHolder(val box: View,val onCorsoListener: OnCorsoListener) : RecyclerView.ViewHolder(box) , View.OnClickListener {
        init{
            box.setOnClickListener(this)

        }
        val cardpopular = box.findViewById<CardView>(R.id.cardpopular)
        override fun onClick(v: View?) {
            onCorsoListener.onCorsoClick(adapterPosition,v)
        }

    }

    //per creare un layout di item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapterViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_corso,
            parent, false
        )
        return MyAdapterViewHolder(layout, monPopularAdapter)
    }

    //quando si ricicla un layout di item aggiornando i dati mostrati all’interno
    override fun onBindViewHolder(holder: MyAdapterViewHolder, position: Int) {
        holder.cardpopular.findViewById<TextView>(R.id.corsoId).text =
            data.get(position).id
        holder.cardpopular.findViewById<TextView>(R.id.textView3).text =
            data.get(position).titolo
        holder.cardpopular.findViewById<TextView>(R.id.categoriaCorsoCard).text =
            data.get(position).categoria
        try {
            System.out.println("immagine:"+(data.get(position).immagine) )
            Picasso.get().load(data.get(position).immagine).into(holder.cardpopular.findViewById<ImageView>(
                R.id.imageView
            ))
        }catch (e:Exception){
            Picasso.get().load("https://png.pngtree.com/png-vector/20191120/ourmid/pngtree-training-course-online-computer-chat-flat-color-icon-vector-png-image_2007114.jpg").into(holder.cardpopular.findViewById<ImageView>(
                R.id.imageView
            ))
        }


    }

    //numero totale di item nella lista
    override fun getItemCount(): Int = data.size

    //interfaccia per poter definire il metodo onclick da altre parti
    interface OnCorsoListener{
        fun onCorsoClick(position: Int, v: View?)
    }
}