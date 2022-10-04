package com.example.progettomobilecamillonitisenigiri.Corso

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettomobilecamillonitisenigiri.Adapters.DispenseAdapter
import com.example.progettomobilecamillonitisenigiri.Model.Documento
import com.example.progettomobilecamillonitisenigiri.R
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection
import com.google.android.material.appbar.MaterialToolbar


class FragmentDispenseCorso : Fragment(), DispenseAdapter.OnDispenseAdapterListener {

    val firebaseConnection: FirebaseConnection by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dispense_corso, container, false)
    }
    override fun onResume() {
        super.onResume()
        val id = requireActivity().intent.getStringExtra("ID_CORSO").toString()
        //recyclerView
        val rvDispense: RecyclerView? = view?.findViewById(R.id.recyclerViewDispense)

        rvDispense?.layoutManager = LinearLayoutManager(
            context,
            LinearLayoutManager.VERTICAL,
            false
        )
        rvDispense?.adapter = DispenseAdapter(ArrayList<Documento>(), this) //inizializzo la rv vuota

        //popolo la rv con le dispense del corso
        firebaseConnection.getListaDispense().observe(
            viewLifecycleOwner,
            Observer<HashMap<String, ArrayList<Documento>>> { dispense ->
                rvDispense?.adapter = DispenseAdapter(dispense.getValue(id).toList(), this)
                //Abilita/disabilita la sezione a seconda dell'iscrizione dell'utente
                if(!firebaseConnection.isIscritto(id)){
                    view?.findViewById<ConstraintLayout>(R.id.paginaNonVisualizzabile)?.visibility =
                        View.VISIBLE
                    view?.findViewById<ConstraintLayout>(R.id.paginaVisualizzabileIscritti)?.visibility =
                        View.GONE
                }
            })
    }

    //override della funzione definita in DispenseAdapter
    override fun onDispenseClick(position: Int, view: View?) {
        val url = view?.findViewById<TextView>(R.id.urlDocumento)?.text
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url as String?))
        startActivity(browserIntent)
    }
}