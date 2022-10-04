package com.example.progettomobilecamillonitisenigiri.Corso

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.progettomobilecamillonitisenigiri.Model.Corso
import com.example.progettomobilecamillonitisenigiri.Model.User
import com.example.progettomobilecamillonitisenigiri.R
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.appbar.MaterialToolbar
import com.squareup.picasso.Picasso


class FragmentInfoCorso : Fragment() {
    //viewModel e dbConnection
    val firebaseConnection: FirebaseConnection by viewModels()

    //variabile boolean per salvare lo stato in fase di aggiornamente della recensione
    private var ratingBarIsInitialized = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_info_corso, container, false)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onResume() {
        super.onResume()
        val id = requireActivity().intent.getStringExtra("ID_CORSO").toString() //prende l'id del corso corrente dai parametri dell'intent
        firebaseConnection.getListaCorsi()
            .observe(viewLifecycleOwner, Observer<List<Corso>> { corsi ->
                //cerca il corso e riempe i campi ui
                for (a in corsi)
                    if (a.id.equals(id)) {
                        view?.findViewById<TextView>(R.id.nomeCorso)?.text = a.titolo
                        view?.findViewById<TextView>(R.id.descrizioneCorso)?.text = a.descrizione
                        view?.findViewById<TextView>(R.id.categoriaCorso)?.text = a.categoria
                        view?.findViewById<TextView>(R.id.numLezioni)?.text =
                            a.lezioni.size.toString()
                        view?.findViewById<TextView>(R.id.docenteCorso)?.text = a.docente
                        view?.findViewById<TextView>(R.id.textView15)?.text = a.prezzo

                        //RatingBar mostrata nella pagina del corso
                        val recensioniBar = view?.findViewById<RatingBar>(R.id.rating)
                        // Disabilito la ratingBar
                        recensioniBar?.setIsIndicator(true)

                        //Inizializzo la ratingBar con il valore medio delle recensioni associate corso
                        if (!ratingBarIsInitialized) {
                            recensioniBar?.rating =
                                a.recensioni.values.average().toFloat()
                            if (a.recensioni.size == 1)
                                view?.findViewById<TextView>(R.id.textView2)?.text =
                                    "(${a.recensioni.size.toString()} Recensione)"
                            else view?.findViewById<TextView>(R.id.textView2)?.text =
                                "(${a.recensioni.size.toString()} Recensioni)"
                            ratingBarIsInitialized = true
                        }

                        //al click sulla ratingBar apre un alertDialog per lasciare/modificare una recensione, solo se l'utente è iscritto
                        recensioniBar
                            ?.setOnTouchListener(View.OnTouchListener { v, event ->
                                if (event.action == MotionEvent.ACTION_UP) {
                                    //se l'utente è iscritto al corso può inserire una recensione
                                    if (firebaseConnection.getUser().value!!.iscrizioni.contains(id)) {
                                        //creo l'alert dialog e ne setto i parametri
                                        val alertDialogAdd = AlertDialog.Builder(context)

                                        val linearLayout = LinearLayout(alertDialogAdd.context)
                                        val ratingBarAlertDialog = RatingBar(linearLayout.context)
                                        val lp = LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.WRAP_CONTENT,
                                            LinearLayout.LayoutParams.WRAP_CONTENT,

                                        )
                                        linearLayout.gravity = Gravity.CENTER
                                        lp.gravity= Gravity.LEFT

                                        ratingBarAlertDialog.layoutParams = lp
                                        ratingBarAlertDialog.numStars = 5
                                        ratingBarAlertDialog.setIsIndicator(false)


                                        linearLayout.addView(ratingBarAlertDialog)

                                        //Se l'utente è iscritto al corso la ratingBar viene abilitata e avrà la possibilià di dare una recensione

                                        alertDialogAdd.setView(linearLayout)
                                        alertDialogAdd.setTitle("Aggiungi/Modifica recensione")
                                        alertDialogAdd.setMessage("Lascia qui la tua recensione, o aggiorna la tua recensione sul corso!")
                                        alertDialogAdd.setPositiveButton(
                                            "AGGIUNGI/MODIFICA",
                                            DialogInterface.OnClickListener() { dialog, which ->

                                                firebaseConnection.setRecensione(
                                                    id,
                                                    ratingBarAlertDialog.rating
                                                )
                                                recensioniBar?.rating =
                                                    a.recensioni.values.average()
                                                        .toFloat() //setto il valore medio delle recensioni presenti
                                                if (a.recensioni.size == 1)
                                                    view?.findViewById<TextView>(R.id.textView2)?.text =
                                                        "(${a.recensioni.size.toString()} Recensione)"
                                                else view?.findViewById<TextView>(R.id.textView2)?.text =
                                                    "(${a.recensioni.size.toString()} Recensioni)"
                                                Toast.makeText(context,"Grazie per la sua recensione",Toast.LENGTH_SHORT).show()

                                            })
                                        alertDialogAdd.setNegativeButton("ANNULLA", null)
                                        alertDialogAdd.show()
                                    }
                                    else{
                                        Toast.makeText(context,"Per recensire il corso devi essere iscritto!",Toast.LENGTH_SHORT).show()
                                    }
                                }

                                return@OnTouchListener true
                            })

                        //carica l'immagine del corso se presente
                        try {
                            Picasso.get().load(a.immagine)
                                .into(view?.findViewById<ImageView>(R.id.imageCorso))
                        } catch (e: Exception) {
                            Log.d("Eccezione", "Eccezione: Problemi caricamento immagine")
                            Picasso.get()
                                .load("https://png.pngtree.com/png-vector/20191120/ourmid/pngtree-training-course-online-computer-chat-flat-color-icon-vector-png-image_2007114.jpg")
                                .into(view?.findViewById<ImageView>(R.id.imageCorso))
                        }

                    }
            })
        //Permette di mantenere aggiornate le funzionalità dell'iscrizione e della wishlist dell'utente
        firebaseConnection.getUser().observe(viewLifecycleOwner,Observer<User>{
            //Aggiorno il bottone di iscrizione a seconda se l'utente è iscritto o meno
            if (firebaseConnection.getUser().value!!.iscrizioni.contains(id)) {
                view?.findViewById<Button>(R.id.iscrivitiButton)?.text =
                    "ANNULLA ISCRIZIONE"
                view?.findViewById<Button>(R.id.iscrivitiButton)?.setTextColor(
                    -65536
                )
            } else {
                view?.findViewById<Button>(R.id.iscrivitiButton)?.text = "ISCRIVITI"
                view?.findViewById<Button>(R.id.iscrivitiButton)?.setTextColor(
                    -10354450
                )
            }
            view?.findViewById<Button>(R.id.iscrivitiButton)?.setOnClickListener {
                firebaseConnection.iscriviti(id) //al click del bottone l'utente viene iscritto/tolto al/dal corso
            }

            //Operazioni su bottone wishlist
            if (firebaseConnection.getUser().value!!.wishlist.contains(id)) {
                view?.findViewById<ImageButton>(R.id.bottoneWishlist)
                    ?.setImageResource(R.drawable.wishlistfull36dp)
            } else {
                view?.findViewById<ImageButton>(R.id.bottoneWishlist)
                    ?.setImageResource(R.drawable.wishlistempty36dp)

            }
            view?.findViewById<ImageButton>(R.id.bottoneWishlist)?.setOnClickListener {
                firebaseConnection.wishlist(id) //al click si aggiunge/elimina il corso dalla wishlist
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val id = arguments?.getString("ID_CORSO")
        if (id != null) {
            Log.d("CiaoId", id)
        }


    }

}