package com.example.progettomobilecamillonitisenigiri.Main

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettomobilecamillonitisenigiri.Adapters.CorsoAdapter
import com.example.progettomobilecamillonitisenigiri.Corso.CorsoActivity
import com.example.progettomobilecamillonitisenigiri.Model.Corso
import com.example.progettomobilecamillonitisenigiri.Model.UltimaLezione
import com.example.progettomobilecamillonitisenigiri.R
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.utils.YouTubePlayerTracker


class FragmentHome : Fragment(R.layout.fragment_home), CorsoAdapter.OnCorsoListener {

    var list = ArrayList<Corso>() //lista di corsi

    lateinit var mProgressbar: ProgressDialog

    var isTheFirstTime = true

    //view model e db connection
    val firebaseConnection: FirebaseConnection by viewModels()

    //tracker per Ultima lezione nella home
    val tracker = YouTubePlayerTracker()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val rvPopolari: RecyclerView = view.findViewById(R.id.recyclerViewPopolari)
        val rvConsigliati: RecyclerView = view.findViewById(R.id.recyclerViewConsigliati)
        val rvRecenti: RecyclerView = view.findViewById(R.id.recyclerViewRecenti)

        //caricamento dei corsi se non è la prima volta
        if(isTheFirstTime){
            mProgressbar = ProgressDialog(context)
            mProgressbar!!.setMessage("Sto caricando i corsi...")
            mProgressbar.show()
            //isTheFirstTime = false
        }

        //Recycler View
        rvPopolari.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvConsigliati?.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvRecenti.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        //Popolamento Recycler View con liste vuote
        rvPopolari.adapter = CorsoAdapter(ArrayList<Corso>(), this)
        rvConsigliati?.adapter = CorsoAdapter(ArrayList<Corso>(), this)
        rvRecenti.adapter =CorsoAdapter(ArrayList<Corso>(), this)

        //Chiamata al getListaCorsi che ritorna una mutableLiveData di List<Corso>
        firebaseConnection.getListaCorsi().observe(
            viewLifecycleOwner,
            Observer<List<Corso>> { corsi ->
                //Ricerca dell'ultima lezione dell'utente corrente
                populateLastLessonPlayer()

                //Passaggio dei corsi alle recyclerView
                rvPopolari.adapter = CorsoAdapter(firebaseConnection.getListaPopolari(corsi as ArrayList<Corso>) , this)
                rvConsigliati?.adapter = CorsoAdapter(
                    firebaseConnection.getListaConsigliati(corsi as ArrayList<Corso>),
                    this
                )
                var AggiuntiDiRecente = ArrayList<Corso>();
                for(corso in corsi){
                    if(corso.id.toInt() >= corsi.size-5)
                    AggiuntiDiRecente.add(corso);
                }
                rvRecenti.adapter = CorsoAdapter(AggiuntiDiRecente, this)
                //Nascondo la progressBar alla fine del caricamento dei corsi
                if(isTheFirstTime ) {
                    mProgressbar.hide()
                    isTheFirstTime=false
                }
            })




    }

    override fun onResume() {
        super.onResume()
        if (!isTheFirstTime)
            mProgressbar.cancel()

    }
    //Al click su un corso viene passato alla CorsoActivity l'id del corso cliccato
    override fun onCorsoClick(position: Int, v: View?) {
        val intent = Intent(context, CorsoActivity::class.java)
        intent.putExtra("ID_CORSO", v?.findViewById<TextView>(R.id.corsoId)?.text)
        startActivity(intent)
    }

    //funzione che aggiunge l'ultima lezione al database
    fun addUltimaLezione(urlLezione: String?, idLezione: String, seconds: Float, idCorso: String?) {
        val utente = firebaseConnection.getUser().value
        var ultimeLezioni = utente!!.ultimeLezioni
        for (lastLesson in ultimeLezioni) { //si cerca se l'utente già aveva una ultima lezione per un corso, se si la elimina
            if (lastLesson.id_corso == idCorso) {
                utente.ultimeLezioni.remove(lastLesson)
                break
            }
        }
        utente?.ultimeLezioni?.add( //aggiunta dell'ultima lezione alle ultime lezioni dell'utente
            UltimaLezione(
                utente.ultimaLezione.id_corso,
                urlLezione,
                idLezione,
                seconds
            )
        )
        utente?.ultimaLezione = UltimaLezione( //aggiunta dell'utlima lezione alla ultima lezione vista dell'utente
            utente.ultimaLezione.id_corso,
            urlLezione,
            idLezione,
            seconds
        )
        firebaseConnection.setUtente(utente)
    }

    //funzione per visualizzare l'ultima lezione se presente
    fun populateLastLessonPlayer() {
        val user = firebaseConnection.getUser().value
        val latestVideoView =
            view?.findViewById<com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView>(
                R.id.videoView
            )
        user?.firstName?.let { Log.d("prova", it) }
        if(!user?.ultimaLezione?.id_corso.equals("")) { //se l'utente ha un'ultima lezione mostro il player
            val titolo = view?.findViewById<TextView>(R.id.lezione)
            val card = view?.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card)
            titolo?.visibility = View.VISIBLE
            card?.visibility = View.VISIBLE
            latestVideoView?.addYouTubePlayerListener(object :
                AbstractYouTubePlayerListener() {
                override fun onReady(lastYouTubePlayer: YouTubePlayer) {
                    lastYouTubePlayer.addListener(tracker)
                    val listaLezioni =
                        firebaseConnection.getListaLezioni().value?.get(user?.ultimaLezione?.id_corso)

                    user?.ultimaLezione?.url_lezione?.let {
                        lastYouTubePlayer.cueVideo(
                            it,
                            user?.ultimaLezione?.secondi
                        )
                    }
                    if (listaLezioni != null) {
                        for (lezione in listaLezioni) {
                            if (lezione.id == user?.ultimaLezione?.id_lezione) { //popolo le view del player con gli attributi del corso corrispondente all'ultima lezione
                                view?.findViewById<TextView>(R.id.titoloLezione)?.text =
                                    lezione.titolo
                                view?.findViewById<TextView>(R.id.descrizioneLezione)?.text =
                                    lezione.descrizione
                                view?.findViewById<TextView>(R.id.idLezioneHidden)?.text =
                                    lezione.id
                                view?.findViewById<com.google.android.material.button.MaterialButton>(
                                    R.id.vaiAlCorso
                                )?.setOnClickListener() {
                                    val intent = Intent(context, CorsoActivity::class.java)
                                    intent.putExtra("ID_CORSO", user?.ultimaLezione?.id_corso)
                                    startActivity(intent)
                                }
                            }
                        }
                    }
                }

                //quando il video viene fatto partire o messo in pausa viene messo come ultima lezione
                override fun onStateChange(
                    youTubePlayer: YouTubePlayer,
                    state: PlayerConstants.PlayerState
                ) {
                    if (state == PlayerConstants.PlayerState.PAUSED) {
                        user?.ultimaLezione?.id_lezione?.let {
                            addUltimaLezione(
                                tracker.videoId,
                                it, tracker.currentSecond, user?.ultimaLezione?.id_corso
                            )
                        }
                    }
                }
            })
        }
        else{ //se non c'è un'ultima lezione toglie il player
            val titolo = view?.findViewById<TextView>(R.id.lezione)
            val card = view?.findViewById<com.google.android.material.card.MaterialCardView>(R.id.card)
            titolo?.visibility = View.GONE
            card?.visibility = View.GONE


        }
    }
}