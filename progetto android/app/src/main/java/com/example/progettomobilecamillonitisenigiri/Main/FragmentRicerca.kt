package com.example.progettomobilecamillonitisenigiri.Main

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.progettomobilecamillonitisenigiri.Adapters.CorsoAdapter
import com.example.progettomobilecamillonitisenigiri.Corso.CorsoActivity
import com.example.progettomobilecamillonitisenigiri.Model.Corso
import com.example.progettomobilecamillonitisenigiri.R
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection


class FragmentRicerca : Fragment(R.layout.fragment_ricerca), CorsoAdapter.OnCorsoListener {

    //argomento della ricerca
    val args: FragmentRicercaArgs by navArgs()
    //view model e db connection
    val firebaseConnection:FirebaseConnection by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ricerca, container, false)

        //metto il bottone per tornare indietro nella appbar
        val toolbar: Toolbar = activity?.findViewById<View>(R.id.topAppBar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.torna_indietro)
        toolbar.setNavigationOnClickListener {
            toolbar.navigationIcon = null
            requireActivity().onBackPressed()
        }
        return view
    }

    override fun onPause() {
        super.onPause()
        //tolgo il bottone di indietro dall'appbar
        val toolbar: Toolbar = activity?.findViewById<View>(R.id.topAppBar) as Toolbar
        toolbar.navigationIcon = null
    }

    override fun onResume(){
        super.onResume()
        //rimetto il bottone per tornare indietro nella appbar
        val toolbar: Toolbar = activity?.findViewById<View>(R.id.topAppBar) as Toolbar
        toolbar.setNavigationIcon(R.drawable.torna_indietro)
        toolbar.setNavigationOnClickListener {
            toolbar.navigationIcon = null
            requireActivity().onBackPressed()
        }
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val query = args.query
        view.findViewById<TextView>(R.id.risRicerca).text = "'$query'"
        //recyclerview ricerca
        val rvSearch = view.findViewById<RecyclerView>(R.id.risultatiSearch)
        rvSearch.layoutManager = GridLayoutManager(context, 2) //grid layout
        rvSearch.adapter = CorsoAdapter(ArrayList<Corso>(), this) //inizializza la rv vuota

        //get dei corsi che vengono poi filtrati in base alla ricerca
        firebaseConnection.getListaCorsi().observe(
            viewLifecycleOwner,
            Observer<List<Corso>> { corsi ->
                rvSearch.adapter = CorsoAdapter(corsi.filter { corso -> //popola la rv con i corsi che rispettano la query
                    corso.titolo.contains(
                        query,
                        true
                    ) || corso.descrizione.contains(query, true) || corso.categoria.contains(
                        query,
                        true
                    )
                }, this)
            })
    }

    //override della funzione definita in CorsoAdapter
    override fun onCorsoClick(position: Int, view: View?) {
        val intent = Intent(context, CorsoActivity::class.java)
        intent.putExtra("ID_CORSO", view?.findViewById<TextView>(R.id.corsoId)!!.text)
        startActivity(intent)
    }
}