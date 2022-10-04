package com.example.progettomobilecamillonitisenigiri.Main

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettomobilecamillonitisenigiri.Adapters.CorsoAdapter
import com.example.progettomobilecamillonitisenigiri.Corso.CorsoActivity
import com.example.progettomobilecamillonitisenigiri.Model.Corso
import com.example.progettomobilecamillonitisenigiri.R
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection

class FragmentFavorites: Fragment(R.layout.fragment_favorites),
    CorsoAdapter.OnCorsoListener {
    //viewmodel e db connection
    val firebaseConnection : FirebaseConnection by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //recycler view
        val rvFavorites: RecyclerView = view.findViewById(R.id.recyclerViewFavorites)
        rvFavorites.layoutManager = GridLayoutManager(context,2) //grid layout
        rvFavorites.adapter = CorsoAdapter(ArrayList<Corso>(),this) //inizializza la rv vuota
        firebaseConnection.getListaCorsi().observe(viewLifecycleOwner, Observer<List<Corso>> { corsi ->
            rvFavorites.adapter = CorsoAdapter(firebaseConnection.getCorsiFromWishlist(corsi as ArrayList<Corso>),this) //popola la rv
        })
    }

    //override della funzione definita su CorsoAdapter
    override fun onCorsoClick(position: Int, v: View?) {
        val intent = Intent(context, CorsoActivity::class.java)
        intent.putExtra("ID_CORSO", v?.findViewById<TextView>(R.id.corsoId)?.text)
        startActivity(intent)
    }
}