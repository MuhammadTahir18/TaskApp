package com.HISkyTech.LoginScreen.Ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.HISkyTech.LoginScreen.Models.task_model
import com.HISkyTech.LoginScreen.R
import com.HISkyTech.LoginScreen.databinding.ActivityHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var dialog: AlertDialog
    private lateinit var sharedPreferences: SharedPreferences
    lateinit var drawerLayout: DrawerLayout
    lateinit var navigationView: NavigationView
    lateinit var toolbar: Toolbar
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        sharedPreferences = getSharedPreferences("user_preferences", Context.MODE_PRIVATE)
        val editor=sharedPreferences.edit()


        drawerLayout = findViewById(R.id.drawerlayout)
        navigationView = findViewById(R.id.navigationView)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.open, R.string.close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()



        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener { menuItem ->
            // Handle navigation item clicks here
            when (menuItem.itemId) {
                R.id.home -> {
                    Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@Home,Home::class.java))

                }

                R.id.contact -> {
                    Toast.makeText(this, "Contact us", Toast.LENGTH_SHORT)
                        .show()
                }
                R.id.fav -> {
                    Toast.makeText(this, "Favorite", Toast.LENGTH_SHORT)
                        .show()
                }

                R.id.share -> {
                    Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
                }
                R.id.rat -> {
                    Toast.makeText(this, "Rate Us", Toast.LENGTH_SHORT).show()
                }
                R.id.theme -> {
                    Toast.makeText(this, "Theme", Toast.LENGTH_SHORT)
                        .show()
                }

                R.id.noti -> {
                    Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show()
                }
                R.id.rem -> {
                    Toast.makeText(this, "Reminder", Toast.LENGTH_SHORT).show()
                }

                R.id.logout-> {
                    editor.putBoolean("IsLoggedIn",false)
                    editor.apply()
                    startActivity(Intent(this@Home,Login::class.java))
                    Toast.makeText(this, "logout successfull", Toast.LENGTH_SHORT).show()

                }
            }
            drawerLayout.closeDrawers()
            true

    }

        binding.addTask.setOnClickListener {
            showChoiceDialog()
        }}
    private fun showChoiceDialog() {
        val builder = AlertDialog.Builder(this)

        builder.setTitle("Select Option")
            .setPositiveButton("Add Task") { dialog, which ->
                addTask()
            }
            .setNegativeButton("Cancel") { dialog, which ->
                dialog.dismiss()
            }

        dialog = builder.create()
        dialog.show()
    }

    private fun addTask() { // Dismiss the choice dialog

        val dialog = Dialog(this, R.style.FullWidthDialog)
        dialog.setContentView(R.layout.add_task_dialog)
        dialog.setCancelable(false)
        dialog.show()

        var title = dialog.findViewById<EditText>(R.id.title)
        val description = dialog.findViewById<EditText>(R.id.descript)
        val date = dialog.findViewById<EditText>(R.id.date)
        val catagory = dialog.findViewById<EditText>(R.id.catagory)
        val priority = dialog.findViewById<EditText>(R.id.priority)
        val back = dialog.findViewById<ImageView>(R.id.back)
        val next = dialog.findViewById<Button>(R.id.btnNext) // Corrected reference
        dialog.setCancelable(false)

        back.setOnClickListener {
            dialog.dismiss()
        }
        // ...


// Check if 'next' is not null before setting the click listener
        next.setOnClickListener {
            if (title.text.toString().isEmpty() || description.text.toString().isEmpty() ||
                date.text.toString().isEmpty() || catagory.text.isEmpty() || priority.text.isEmpty()
            ) {
                Toast.makeText(this, "Please Enter All fields", Toast.LENGTH_SHORT).show()
            } else {
                val model = task_model()
                model.title = title.text.toString()
                model.description = description.text.toString()
                model.task_date = date.text.toString()
                model.Catagory = catagory.text.toString()
                model.priority = priority.text.toString()

                taskAdd(model)
                dialog.dismiss()


            }
        }
// ...

    }


    private fun taskAdd(model: task_model) {

        val sharedPreferences = getSharedPreferences("preference", Context.MODE_PRIVATE)

        db.collection("Tasks").add(model).addOnSuccessListener { document ->
            model.task_id = document.id
            model.userId = sharedPreferences.getString("userid", "").toString()

            db.collection("Tasks").document(document.id).set(model)
                .addOnSuccessListener {
                    Toast.makeText(this, "Task added", Toast.LENGTH_SHORT).show()

                }
                .addOnFailureListener {
                    Toast.makeText(this, "Task not added", Toast.LENGTH_SHORT).show()
                }
        }
    }
}
