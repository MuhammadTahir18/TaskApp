package com.HISkyTech.LoginScreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.HISkyTech.LoginScreen.databinding.ActivitySignupBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class signup : AppCompatActivity() {
    private lateinit var binding:ActivitySignupBinding
    private var db= Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        binding= ActivitySignupBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)




    }}
