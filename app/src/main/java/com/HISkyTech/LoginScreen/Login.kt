package com.HISkyTech.LoginScreen

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.HISkyTech.LoginScreen.databinding.ActivityLoginBinding
import com.HISkyTech.LoginScreen.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.flow.combine

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private var db= Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
binding.signup.setOnClickListener(){
    startActivity(Intent(this,signup::class.java))
}


        binding.lgn.setOnClickListener() {

            val email = binding.email.text.toString()
            val password = binding.password.text.toString()


            db.collection("users").whereEqualTo("email",email)
                .whereEqualTo("password",password)
                .get()
                .addOnSuccessListener{
                    document->
                    Toast.makeText(this, "login successfull", Toast.LENGTH_SHORT).show()
                    var model1=loginmodel()

                    for (document in document){
                        model1=document.toObject(loginmodel::class.java)
                    }

                    val sharedPreferences = getSharedPreferences("Name", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userId", model1.docID)
                    editor.apply()

                    startActivity(Intent(this@Login,Home::class.java))



                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Login failed: ${exception.message}", Toast.LENGTH_SHORT)
                        .show()
                }

                }

        }
    }