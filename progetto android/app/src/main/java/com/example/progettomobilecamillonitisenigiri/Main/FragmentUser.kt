package com.example.progettomobilecamillonitisenigiri.Main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.progettomobilecamillonitisenigiri.Model.User
import com.example.progettomobilecamillonitisenigiri.R
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*


class FragmentUser : Fragment() {
    //Firebase
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null

    //UI elements
    private var tvFirstName: EditText? = null
    private var tvLastName: EditText? = null
    private var tvEmail: TextView? = null
    lateinit var mSaveBtn: Button
    lateinit var chipGroup1: ChipGroup
    lateinit var chipGroup2: ChipGroup
    //Lista categorie Corsi
    val listaCategorie: ArrayList<String> = ArrayList()


    //viewModel e dbConnection
    val firebaseConnection: FirebaseConnection by viewModels()
    //variabile booleana per mantenere lo stato dello switch del tema
    var checked = false


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        //istanze dell'utente
        mAuth = FirebaseAuth.getInstance()
        mUser = mAuth!!.currentUser

        // context?.resources?.configuration?.uiMode restituisce 17 in nightModeOFF e 33 in nightModeON
        checked = context?.resources?.configuration?.uiMode != 17
        if (checked)
            view.findViewById<Switch>(R.id.switch1).text = "ON"
        else
            view.findViewById<Switch>(R.id.switch1).text = "OFF"
        view.findViewById<Switch>(R.id.switch1).isChecked = checked

        //Switch della modalità notturna
        view.findViewById<Switch>(R.id.switch1).setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                // The switch is enabled/checked
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                // The switch is disabled
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            }
            checked=isChecked
        }


        //Chipgroup delle categorie dove specificare le categorie preferite
        chipGroup1 = view.findViewById<ChipGroup>(R.id.chipGroupUser1)
        chipGroup2 = view.findViewById<ChipGroup>(R.id.chipGroupUser2)

        //chiamata al view model
        firebaseConnection.getCategorie().observe(viewLifecycleOwner, Observer<Set<String>> { categorie ->

            //ottengo dati utente e popola EditText
            tvFirstName!!.setText(firebaseConnection.getUser().value?.firstName)
            tvLastName!!.setText(firebaseConnection.getUser().value?.lastName)
            tvEmail!!.text = mUser!!.email

            //Pulizia chipgroup
            chipGroup1.removeAllViews()
            chipGroup2.removeAllViews()

            listaCategorie.clear()
            listaCategorie.addAll(firebaseConnection.getUser().value!!.categoriePref) //assegnazione categorie utente

            //popolo le chip
            for (i in 0..categorie.size - 1) {
                var chip = inflater.inflate(R.layout.chip_catalogo, chipGroup1, false) as Chip
                var chip2 = inflater.inflate(R.layout.chip_catalogo, chipGroup2, false) as Chip
                if (i % 2 == 0) {
                    chip.id = i
                    chip.text = categorie.elementAt(i)
                    chip.isCheckable = true
                    //se la categoria è contenuta nelle categoria preferite dell'utente la mette checkata
                    if (listaCategorie.contains(categorie.elementAt(i)))
                        chip.isChecked = true

                    //click listener per il check delle chips
                    chipGroup1.setOnCheckedChangeListener { group, checkedId ->
                        view.findViewById<Chip>(checkedId).isChecked =
                            !view.findViewById<Chip>(checkedId).isChecked
                    }
                    chipGroup1.addView(chip)
                } else {
                    chip2.id = i
                    chip2.text = categorie.elementAt(i)
                    chip2.isCheckable = true
                    //se la categoria è contenuta nelle categoria preferite dell'utente la mette checkata
                    if (listaCategorie.contains(categorie.elementAt(i)))
                        chip2.isChecked = true

                    //click listener per il check delle chips
                    chipGroup2.setOnCheckedChangeListener { group, checkedId ->
                        view.findViewById<Chip>(checkedId).isChecked =
                            !view.findViewById<Chip>(checkedId).isChecked
                    }
                    chipGroup2.addView(chip2)
                }
            }
        })

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //elementi ui
        tvFirstName = view?.findViewById<View>(R.id.tv_first_name) as EditText
        tvLastName = view?.findViewById<View>(R.id.tv_last_name) as EditText
        tvEmail = view?.findViewById<View>(R.id.tv_email) as TextView
        mSaveBtn = view?.findViewById(R.id.save_btn) as Button

        //Bottone salva dei campi dell'utente
        mSaveBtn.setOnClickListener {
            listaCategorie.clear()
            val utente: User = firebaseConnection.getUser().value as User
            utente.categoriePref.clear()
            utente.firstName = tvFirstName!!.text.toString()
            utente.lastName = tvLastName!!.text.toString()

            //metto tutte le chips checkate nella variabile listaCategorie
            for (id in chipGroup1.checkedChipIds) {
                var chip = view?.findViewById<Chip>(id)
                listaCategorie.add(chip!!.text.toString())
            }
            for (id in chipGroup2.checkedChipIds) {
                var chip = view?.findViewById<Chip>(id)
                listaCategorie.add(chip!!.text.toString())
            }

            utente.categoriePref.addAll(listaCategorie)

            firebaseConnection.setUtente(utente) //salva l'utente e viene aggiornato nel database

            Toast.makeText(context, "Modifiche salvate correttamente", Toast.LENGTH_LONG).show()
        }


    }



}