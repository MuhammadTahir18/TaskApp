package com.HISkyTech.LoginScreen.Ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import com.HISkyTech.LoginScreen.R

class Splash_screen : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 2000
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPref: SharedPreferences = getSharedPreferences("preference", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()


        Handler().postDelayed({
            if (!sharedPref.getBoolean("IsLog",false))
            {
                Toast.makeText(this, sharedPref.getBoolean("IsLog",false).toString(), Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, Login::class.java))
            } else {
                startActivity(Intent(this, MainActivity::class.java))
            }
            finish()
        },SPLASH_TIME_OUT)
    }


}


