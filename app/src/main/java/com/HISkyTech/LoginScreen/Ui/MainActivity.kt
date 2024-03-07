package com.HISkyTech.LoginScreen.Ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.HISkyTech.LoginScreen.R
import com.HISkyTech.LoginScreen.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        binding=ActivityMainBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        binding.nextbtn.setOnClickListener{
            startActivity(Intent(this,Home::class.java))
            finish()
        }
    }
}