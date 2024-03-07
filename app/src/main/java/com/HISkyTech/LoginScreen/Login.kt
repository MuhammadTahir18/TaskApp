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
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLoginBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.signup.setOnClickListener() {
            startActivity(Intent(this, signup::class.java))
        }


        binding.apply {

            lgn.setOnClickListener {
                if (email.text.toString().isEmpty() && password.text.toString().isEmpty()) {
                    Toast.makeText(this@Login, "Please fill email and password", Toast.LENGTH_SHORT).show()
                } else {





                    db.collection("User")
                        .whereEqualTo("mail", email.text.toString())
                        .whereEqualTo("pasword", password.text.toString())
                        .get()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val querySnapshot = task.result
                                if (querySnapshot != null && !querySnapshot.isEmpty) {
                                    val documentId = querySnapshot.documents[0].id

                                    val sharedPreferences = getSharedPreferences("preference", Context.MODE_PRIVATE)
                                    val editor = sharedPreferences.edit()
                                    editor.putBoolean("IsLog",true)
                                    editor.apply()
                                    editor.putString("userid", documentId)
                                    editor.apply()

                                    Toast.makeText(this@Login, "Login Successful", Toast.LENGTH_SHORT).show()
                                    startActivity(Intent(this@Login,MainActivity::class.java))
                                    finish()
                                } else {
                                    Toast.makeText(this@Login, "Invalid email or password", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                Toast.makeText(this@Login, "Login Unsuccessful", Toast.LENGTH_SHORT).show()
                            }
                        }
                }
            }




        }


    }
}

