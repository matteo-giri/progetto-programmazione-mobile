package com.example.progettomobilecamillonitisenigiri.Main

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent

import android.os.Bundle
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.progettomobilecamillonitisenigiri.Auth.LoginActivity
import com.example.progettomobilecamillonitisenigiri.Model.User
import com.example.progettomobilecamillonitisenigiri.R
import com.example.progettomobilecamillonitisenigiri.ViewModels.FirebaseConnection

import com.example.progettomobilecamillonitisenigiri.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
/*
* Main Activity
* */

class MainActivity : AppCompatActivity() {

    //istanza dell'autenticazione che viene usata per controllare se l'utente è loggato
    lateinit var mAuth: FirebaseAuth

    //variabile booleana usata per mostrare un alert dialog solo al primo accesso, se non vengono specificate categorie preferite
    var isThefirstTime = true

    //istanziazione viewModel e connesione a firebase
    val firebaseConnection: FirebaseConnection by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState != null) {
            isThefirstTime = savedInstanceState.getBoolean("firstTime")
        }
        //Binding
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        //setup della navigazione per la bottom navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.myNavHostFragment) as NavHostFragment
        val navController = navHostFragment.navController

        findViewById<BottomNavigationView>(R.id.bottom_navigation)
            .setupWithNavController(navController)

        //TopAppBar
        binding.topAppBar.title = "Corsi per Tutti"
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.share -> {
                    val intent= Intent()
                    intent.action=Intent.ACTION_SEND
                    intent.putExtra(Intent.EXTRA_TEXT,"Scopri la nostra piattaforma! Scarica la nostra app!")
                    intent.type="text/plain"
                    startActivity(Intent.createChooser(intent,"Condividi con:"))
                    true
                }
                R.id.logout -> {
                    mAuth = FirebaseAuth.getInstance()
                    FirebaseAuth.getInstance().signOut()
                    val startIntent = Intent(applicationContext, LoginActivity::class.java)
                    startActivity(startIntent)
                    finish()
                    true
                }
                else -> false
            }
        }
    }

    override fun onStart() {
        super.onStart()
        //Istanza dell'utente loggato
        val currentUser = FirebaseAuth.getInstance().currentUser
        //se l'utente non è loggato viene rimandato all'activity di Login
        if (currentUser == null) {
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()

        } else {
            //Controllo se l'utente corrente ha specificato categorie preferite altrimenti mostra un alertDialog
            firebaseConnection.getUser().observe(this, Observer<User> { utente->
                if(isThefirstTime){
                    isThefirstTime = false
                    if(utente.categoriePref.size<1){
                        val alertDialog = AlertDialog.Builder(this)
                        alertDialog.setTitle("Indica le tue Categorie Preferite")
                        alertDialog.setMessage("Indicare le tue categorie preferite servirà all'app per aumentare la tua user experience")
                        alertDialog.setPositiveButton(
                            "OK",
                            DialogInterface.OnClickListener() { dialog, which ->
                                this.findNavController(R.id.myNavHostFragment)
                                    .navigate(R.id.action_FragmentHome_to_FragmentUser)
                            })
                        alertDialog.setNegativeButton("Più Tardi", null)
                        alertDialog.show()
                    }
                }
            })
        }
    }

    // invoked when the activity may be temporarily destroyed, save the instance state here
    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean("firstTime",isThefirstTime) //salva nello stato il valore della variabile booleana
        super.onSaveInstanceState(outState)
    }
}