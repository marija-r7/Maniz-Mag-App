package com.example.manizmagapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {
    private lateinit var  korisnikId: String
    private lateinit var korisnickoIme: String
    private lateinit var imePrezime: String
    private lateinit var email: String
    private  lateinit var password: String
    private lateinit var authorization: FirebaseAuth
    private lateinit var korisnik: Korisnik
    private lateinit var databaseRef: DatabaseReference
    private lateinit var errorText: TextView
    private lateinit var registerBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registeraccount_activity)

        registerBtn = findViewById<Button>(R.id.RegisterBtn)
        registerBtn.setOnClickListener(View.OnClickListener {
            InitializeValues()

            if (checkFieldsNotEmpty(korisnickoIme, imePrezime, email, password))
            {
                if (!Validator.validateEmailFormat(email)) {
                    Toast.makeText(this, "Invalid E-mail format", Toast.LENGTH_SHORT).show()
                    return@OnClickListener
                }
                korisnik = Korisnik(korisnikId, korisnickoIme, imePrezime, email, password)
                errorText.visibility = View.INVISIBLE
                registrujKorisnika(korisnik)
            }
            else
            {
                errorText.text = "You must enter an E-mail address and a password."
                errorText.visibility = View.VISIBLE
            }
        })
    }

    private fun registrujKorisnika(korisnik: Korisnik) {
        authorization.createUserWithEmailAndPassword(korisnik.email, korisnik.password).addOnCompleteListener(this, OnCompleteListener<AuthResult>() {
            if (it.isSuccessful){
                dodajKorisnikaUBazu(korisnik)
            } else {
                Log.e("Firebase","Reg Failed",it.exception)
            }
        } )
    }

    private fun dodajKorisnikaUBazu(korisnik: Korisnik) {
        databaseRef.child(korisnikId).setValue(korisnik)
            .addOnCompleteListener {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                startActivity(Intent(applicationContext, MainActivity::class.java).putExtra("korisnik", korisnik))
            }.addOnFailureListener{ err ->
                Toast.makeText(this, "Registration failed. Error ${err.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkFieldsNotEmpty(korisnickoIme: String, imePrezime: String, email: String, password: String): Boolean {
        if (korisnickoIme.isNotEmpty() && imePrezime.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty())
            return(true)
        else
            return(false)
    }

    private fun InitializeValues()
    {
        korisnickoIme = findViewById<EditText>(R.id.RegUsernameEText).text.toString()
        imePrezime = findViewById<EditText>(R.id.RegFullNameEText).text.toString()
        email = findViewById<EditText>(R.id.RegEmailEText).text.toString()
        password = findViewById<EditText>(R.id.LoginPasswordEText).text.toString()
        errorText = findViewById<TextView>(R.id.ErrorTextLbl)
        authorization = Firebase.auth
        databaseRef = FirebaseDatabase.getInstance().getReference("Korisnici")
        korisnikId = databaseRef.push().key!!
    }
}
