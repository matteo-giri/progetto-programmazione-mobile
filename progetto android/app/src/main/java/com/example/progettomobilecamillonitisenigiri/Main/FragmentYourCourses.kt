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

class FragmentYourCourses: Fragment(R.layout.fragment_personal_courses),
    CorsoAdapter.OnCorsoListener{
    val firebaseConnection : FirebaseConnection by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //recyclerview
        val rvYourCourses:RecyclerView = view.findViewById(R.id.recyclerViewYourCourses)
        rvYourCourses.layoutManager = GridLayoutManager(context,2) //grid layout
        rvYourCourses.adapter = CorsoAdapter(ArrayList<Corso>(),this) //inizializza l'rv vuota
        firebaseConnection.getListaCorsi().observe(viewLifecycleOwner, Observer<List<Corso>> { corsi ->
            rvYourCourses.adapter = CorsoAdapter(firebaseConnection.getCorsiFrequentati(corsi as ArrayList<Corso>),this) //popola l'rv
        })

    }

    //override della funzione definita in CorsoAdapter
    override fun onCorsoClick(position: Int, v: View?) {
        val intent = Intent(context, CorsoActivity::class.java)
        intent.putExtra("ID_CORSO", v?.findViewById<TextView>(R.id.corsoId)?.text)
        startActivity(intent)
    }
}