package com.HISkyTech.LoginScreen

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.HISkyTech.LoginScreen.databinding.ActivityHomeBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private var db= Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }}
