package com.example.manizmagapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmail: String
    private  lateinit var loginPassword: String
    private lateinit var korisnik: Korisnik
    private lateinit var authorization: FirebaseAuth
    private lateinit var databaseRef: DatabaseReference
    private lateinit var emailInput: TextInputEditText
    private lateinit var passwordInput: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        emailInput = findViewById<TextInputEditText>(R.id.LoginEmailEText)
        passwordInput = findViewById<TextInputEditText>(R.id.LoginPasswordEText)

        val registerAccountTView = findViewById<TextView>(R.id.CreateAccTView)
        registerAccountTView.setOnClickListener{
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        val LoginUserBtn = findViewById<Button>(R.id.LoginBtn)
        LoginUserBtn.setOnClickListener{
        InitializeValues()

            if (checkFieldsNotEmpty(loginEmail, loginPassword))
                LoginKorisnika(loginEmail, loginPassword)
        }
    }

    private fun InitializeValues()
    {
        loginEmail = emailInput.text.toString()
        loginPassword = passwordInput.text.toString()
        authorization = Firebase.auth
        databaseRef = FirebaseDatabase.getInstance().getReference("Korisnici")
    }

    private fun checkFieldsNotEmpty(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            emailInput.error = "Please enter an E-mail address."
            return false
        }
        if (password.isEmpty()){
            passwordInput.error = "Please enter a Password."
            return false
        }
        return true
    }

    private fun LoginKorisnika(email: String, password: String)
    {
        authorization.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                InitializeUserFromDatabase(email, password)
            }.addOnFailureListener{
                Toast.makeText(applicationContext, "Login failed. Wrong E-mail or Password. Please try again", Toast.LENGTH_SHORT).show()
            }
    }

    private fun InitializeUserFromDatabase(email: String, password: String)
    {
        databaseRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for(usersnap in snapshot.children)
                    {
                        var storedPassword = usersnap.child("password").value
                        if (storedPassword == password)
                        {
                            korisnik = Korisnik(usersnap.key.toString(),usersnap.child("korisnickoIme").value.toString(),usersnap.child("imePrezime").value.toString(),usersnap.child("email").value.toString(),usersnap.child("password").value.toString())
                            Toast.makeText(applicationContext, "You've logged in successfully.", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(applicationContext, MainActivity::class.java).putExtra("korisnik", korisnik))
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}