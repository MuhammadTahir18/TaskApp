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
binding.sgnin.setOnClickListener(){
    startActivity(Intent(this,Login::class.java))
}
binding.apply {
btnsignup.setOnClickListener(){

   var usermodel=loginmodel()

    if (email.text.toString().isEmpty() && password.text.toString().isEmpty() && name.text.toString().isEmpty()
    ) {
        Toast.makeText(this@signup, "please Fill name and email", Toast.LENGTH_SHORT)
            .show()
    } else if (password.text.toString().length < 6) {
        Toast.makeText(this@signup, "Invalid password format", Toast.LENGTH_SHORT)
            .show()
    } else if (password.text.toString() !=cpassword.text.toString()) {
        Toast.makeText(this@signup, "password not matched", Toast.LENGTH_SHORT).show()
    } else {
       usermodel.mail = email.text.toString()
        usermodel.pasword = password.text.toString()
        usermodel.name = name.text.toString()
        db.collection("User").add(usermodel)
            .addOnSuccessListener { documentreference ->


                usermodel.userid = documentreference.id
                db.collection("User").document(documentreference.id).set(usermodel)


                Toast.makeText(this@signup, "SignUp Successfull", Toast.LENGTH_SHORT)
                    .show()
                   startActivity(Intent(this@signup,Login::class.java))

            }


            .addOnFailureListener()
            {
                Toast.makeText(this@signup, "Failed to SignUp", Toast.LENGTH_SHORT)
                    .show()
            }
    }

}
    }
}

}

