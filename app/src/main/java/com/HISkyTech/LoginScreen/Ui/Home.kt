package com.HISkyTech.LoginScreen.Ui

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.HISkyTech.LoginScreen.Models.task_model
import com.HISkyTech.LoginScreen.R
import com.HISkyTech.LoginScreen.databinding.ActivityHomeBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Home : AppCompatActivity() {
    private lateinit var binding: ActivityHomeBinding
    private lateinit var dialog: AlertDialog
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addTask.setOnClickListener {
            showChoiceDialog()
        }
    }

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
