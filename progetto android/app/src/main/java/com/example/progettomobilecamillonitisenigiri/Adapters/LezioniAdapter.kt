package com.example.progettomobilecamillonitisenigiri.Adapters

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.progettomobilecamillonitisenigiri.Corso.FragmentLezioniCorso
import com.example.progettomobilecamillonitisenigiri.Model.Lezione
import com.example.progettomobilecamillonitisenigiri.R
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
/*
* Adapter delle Lezioni
* */


class LezioniAdapter(
    val data: List<Lezione>,
    val monLezioniAdapter: OnLezioniAdapterListener,
    val view: View?,
    val fragment: FragmentLezioniCorso
): RecyclerView.Adapter<LezioniAdapter.LezioniAdapterViewHolder>() {
    class LezioniAdapterViewHolder(
        val box: View,
        val onLezioniAdapterListener: OnLezioniAdapterListener
    ) : RecyclerView.ViewHolder(box) , View.OnClickListener {
        init{
            box.setOnClickListener(this)
        }
        val cardLezione = box.findViewById<CardView>(R.id.cardViewDocumento)
        override fun onClick(v: View?) {
            onLezioniAdapterListener.onLezioneClick(adapterPosition, v)
        }
    }

    //per creare un layout di item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LezioniAdapterViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(
            R.layout.layout_lezione,
            parent, false
        )
        return LezioniAdapterViewHolder(layout, monLezioniAdapter)
    }

    lateinit var ytView : YouTubePlayerView //video player delle varie lezioni
    lateinit var latestVideoView : YouTubePlayerView //video player dell'ultima lezione vista

    //quando si ricicla un layout di item aggiornando i dati mostrati all’interno
    override fun onBindViewHolder(holder: LezioniAdapterViewHolder, position: Int) {
        holder.cardLezione.findViewById<TextView>(R.id.titoloDelDocumento).text =
            data.get(position).titolo
        holder.cardLezione.findViewById<TextView>(R.id.descrizioneLezione).text =
            data.get(position).descrizione

        //FUNZIONALITA' PER I VIDEO
        val tracker = YouTubePlayerTracker()
        ytView = holder.cardLezione.findViewById<YouTubePlayerView>(R.id.youtubeVideoView)
        ytView.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                val videoId = data.get(position).url
                youTubePlayer.cueVideo(videoId, 0f)
                youTubePlayer.addListener(tracker)
            }

            //quando il video viene fatto partire o messo in pausa viene messo come ultima lezione
            override fun onStateChange(
                youTubePlayer: YouTubePlayer,
                state: PlayerConstants.PlayerState
            ) {
                if (state == PlayerConstants.PlayerState.PAUSED) {
                    fragment.addUltimaLezione(tracker.videoId, data.get(position).id, tracker.currentSecond,true)
                }
                if (state == PlayerConstants.PlayerState.PLAYING) {
                    fragment.addUltimaLezione(tracker.videoId, data.get(position).id, tracker.currentSecond,true)
                }
            }
        })
    }

    //numero totale di item nella lista
    override fun getItemCount(): Int = data.size

    //interfaccia per poter definire il metodo onclick da altre parti
    interface OnLezioniAdapterListener{
        fun onLezioneClick(position: Int, v: View?)
    }
}

