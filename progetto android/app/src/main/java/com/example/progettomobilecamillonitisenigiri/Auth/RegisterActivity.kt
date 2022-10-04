package com.example.progettomobilecamillonitisenigiri.Auth

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.progettomobilecamillonitisenigiri.Main.MainActivity
import com.example.progettomobilecamillonitisenigiri.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

/*
*  Activity che gestisce la fase di registrazione dell'utente non registrato
* La registrazione è gestitata tramite le librerie di Firebase
* */
class RegisterActivity : AppCompatActivity() {

    //elementi ui
    lateinit var mRegisterBtn: Button
    lateinit var mRegisterEmail: EditText
    lateinit var mRegisterPassword: EditText
    lateinit var mRegisterLoginBtn: Button
    lateinit var mProgressbar: ProgressDialog

    private var FirstName: EditText? = null
    private var LastName: EditText? = null

    //Firebase references
    private var mDatabaseReference: DatabaseReference? = null
    private var mDatabase: FirebaseDatabase? = null
    lateinit var mAuth: FirebaseAuth

    private val TAG = "CreateAccountActivity"

    //global variables
    private var firstName: String? = null
    private var lastName: String? = null
    private var email: String? = null
    private var password: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register) //inflating del layout
        initialise()

    }

    private fun initialise() {
        //elementi ui
        FirstName = findViewById<View>(R.id.first_name) as EditText
        LastName = findViewById<View>(R.id.last_name) as EditText
        mRegisterEmail = findViewById<View>(R.id.RegisterEmail) as EditText
        mRegisterPassword = findViewById<View>(R.id.RegisterPassword) as EditText

        mRegisterBtn = findViewById<View>(R.id.RegisterBtn) as Button

        mProgressbar = ProgressDialog(this)

        //istanziazione connesioni db Firebase
        mDatabase = FirebaseDatabase.getInstance()
        mDatabaseReference = mDatabase!!.reference!!.child("Users")
        mAuth = FirebaseAuth.getInstance()  //istanziazione Firebase Auth

        mProgressbar = ProgressDialog(this)

        //LoginBtn lancia activity di login
        mRegisterLoginBtn = findViewById(R.id.RegisterLoginBtn)
        mRegisterLoginBtn.setOnClickListener {
            val loginIntent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(loginIntent)
        }

        //bottone di registrati
        mRegisterBtn!!.setOnClickListener { createNewAccount() }

    }

    //funzione che gestisce la creazione dell'account
    private fun createNewAccount() {
        //get input da tastiera
        firstName = FirstName?.text.toString()
        lastName = LastName?.text.toString()
        email = mRegisterEmail?.text.toString().trim()
        password = mRegisterPassword?.text.toString().trim()
        //inserire qui i controlli sui campi
        if (TextUtils.isEmpty(firstName)||TextUtils.isEmpty(lastName)) {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
            return
        }

        if (TextUtils.isEmpty(email)) {
            mRegisterEmail.error = " Enter Email"
            return
        }
        if (TextUtils.isEmpty(password)) {
            mRegisterPassword.error = " Enter Password"
            return
        }

        if (!TextUtils.isEmpty(firstName) && !TextUtils.isEmpty(lastName)
            && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)
        ) {
        } else {
            Toast.makeText(this, "Enter all details", Toast.LENGTH_SHORT).show()
        }
        mProgressbar!!.setMessage("Registering User...")
        mProgressbar!!.show()
        //creazione utente
        mAuth!!
            .createUserWithEmailAndPassword(email!!, password!!)
            .addOnCompleteListener(this) { task ->
                mProgressbar!!.hide()

                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")

                    val userId = mAuth!!.currentUser!!.uid

                    //Verify Email
                    verifyEmail();

                    //update user profile information
                    val currentUserDb = mDatabaseReference!!.child(userId)
                    currentUserDb.child("firstName").setValue(firstName)
                    currentUserDb.child("lastName").setValue(lastName)

                    updateUserInfoAndUI()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(
                        this@RegisterActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun updateUserInfoAndUI() {
        //start next activity
        val intent = Intent(this@RegisterActivity, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    //funzione che invia un email di verifica
    private fun verifyEmail() {
        val mUser = mAuth!!.currentUser;
        mUser!!.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@RegisterActivity,
                        "Verification email sent to " + mUser.getEmail(),
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.e(TAG, "sendEmailVerification", task.exception)
                    Toast.makeText(
                        this@RegisterActivity,
                        "Failed to send verification email.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}