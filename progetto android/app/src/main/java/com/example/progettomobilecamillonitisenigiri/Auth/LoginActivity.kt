package com.example.progettomobilecamillonitisenigiri.Auth

import android.app.ProgressDialog
import android.content.Intent

import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.progettomobilecamillonitisenigiri.Main.MainActivity
import com.example.progettomobilecamillonitisenigiri.R
import com.google.firebase.auth.FirebaseAuth
/*
*  Activity che gestisce la fase di autenticazione dell'utente già registrato
* L'autenticazione è gestitata tramite le librerie di Firebase
* */
class LoginActivity : AppCompatActivity() {

    //elementi ui dell'activity
    lateinit var mLoginbtn: Button
    lateinit var mLoginRegisterBtn: Button
    lateinit var mLoginEmail: EditText
    lateinit var mLoginPassword: EditText
    lateinit var mProgressbar: ProgressDialog

    // FirebaseAuth da instanziare
    lateinit var mAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) //inflating del layout

        //inizializzazione elementi ui
        mLoginbtn = findViewById(R.id.LoginBtn)
        mLoginRegisterBtn = findViewById(R.id.LoginRegisterBtn)
        mLoginEmail = findViewById(R.id.LoginEmail)
        mLoginPassword = findViewById(R.id.LoginPassword)
        mProgressbar = ProgressDialog(this)

        mAuth = FirebaseAuth.getInstance() //istanziazione FirebaseAuth

        //click sul bottone di Login
        mLoginbtn.setOnClickListener {
            val email = mLoginEmail.text.toString().trim()
            val password = mLoginPassword.text.toString().trim()

            //Check caselle vuote
            if (TextUtils.isEmpty(email)) {
                mLoginEmail.error = " Enter Email"
                return@setOnClickListener
            }

            if (TextUtils.isEmpty(password)) {
                mLoginEmail.error = " Enter Password"
                return@setOnClickListener
            }

            loginUser(email, password) //gestisce login
        }

        //click bottone Registrati
        mLoginRegisterBtn.setOnClickListener {
            val registerActivity = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(registerActivity) //lancia l'activity di registrazione
            finish()
        }

    }

    //funzione che gestisce il login dell'utente
    private fun loginUser(email: String, password: String) {
        //Progressbar login
        mProgressbar.setMessage("Please wait..")
        mProgressbar.show()

        //gestione del login con Firebase tramite email e password
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    mProgressbar.dismiss()
                    val startIntent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(startIntent)
                    finish()
                } else {
                    //gestione utente non registrato
                    Toast.makeText(
                        this,
                        "Authentication failed.${task.exception}",
                        Toast.LENGTH_SHORT
                    ).show()

                }

                mProgressbar.dismiss()
            }
    }
}