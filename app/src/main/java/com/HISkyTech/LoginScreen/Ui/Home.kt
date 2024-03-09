package com.HISkyTech.LoginScreen.Ui

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.compose.ui.text.toUpperCase
import androidx.recyclerview.widget.GridLayoutManager
import com.HISkyTech.LoginScreen.Adapters.AdapterTask
import com.HISkyTech.LoginScreen.Models.task_model
import com.HISkyTech.LoginScreen.R
import com.HISkyTech.LoginScreen.databinding.ActivityHomeBinding
import com.bumptech.glide.Glide
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import java.util.Locale


class Home : AppCompatActivity() ,AdapterTask.OnItemClickListener {
    private lateinit var binding: ActivityHomeBinding
     private lateinit var dialog: Dialog
    private val IMAGE_PICKER_REQUEST_CODE = 123
    private var imageURI: Uri? = null
     private lateinit var itemList: List<task_model>
     private lateinit var filteredList: MutableList<task_model>
     private lateinit var dialogDetail: Dialog
    private var db = Firebase.firestore
    var list= ArrayList<task_model> ()

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityHomeBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)



        binding.iamgetodo.setOnClickListener{
            val pickImage =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(pickImage, IMAGE_PICKER_REQUEST_CODE)
        }








        binding.addTask.setOnClickListener {
            showChoiceDialog()
        }
        setAdaptertask()

        binding.tvSearch.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText != null) {
                    filter(newText)
                }
                return false
            }
        })

    }
    private fun filter(text: String) {
        val filteredList = ArrayList<task_model>()

        for (task in list) {
            val titleInUpperCase = task.title.toUpperCase(Locale.getDefault())
            val searchTextInLowerCase = text.lowercase(Locale.getDefault())

            if (titleInUpperCase.contains(searchTextInLowerCase)) {
                filteredList.add(task)
            }
        }

        if (filteredList.isEmpty()) {
            // If no items are added to the filtered list, display a toast message indicating no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show()
        } else {
            // If there are filtered items, set up a new adapter with the filtered list and update the RecyclerView.
            binding.rv.adapter = AdapterTask(this, filteredList, this@Home)
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
                    setAdaptertask()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Task not added", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun setAdaptertask() {
        val listtask = ArrayList<task_model>()

        db.collection("Tasks").get().addOnSuccessListener { taskResult ->

            if (taskResult != null) {
                if (taskResult.size() > 0) {
                    for (document in taskResult) {
                        listtask.add(document.toObject(task_model::class.java))
                        listtask.sortBy { it.title }
                    }
                }
                binding.rv.layoutManager = GridLayoutManager(this,2)
                binding.rv.adapter = AdapterTask(this, listtask,this )
            }
        }
    }

     @SuppressLint("CutPasteId")
     override fun onItemClick(taskModel: task_model) {
         dialogDetail = Dialog(this, R.style.FullWidthDialog)
         dialogDetail.setContentView(R.layout.dialog_detail_task)
         dialogDetail.setCancelable(false)


         var title = dialogDetail.findViewById<TextView>(R.id.tvFname)
         var description = dialogDetail.findViewById<TextView>(R.id.tvCnic)

         var password = dialogDetail.findViewById<TextView>(R.id.tvaddress)
         var category = dialogDetail.findViewById<TextView>(R.id.tvcategory)
         var priority = dialogDetail.findViewById<TextView>(R.id.tvPriority)
         var date = dialogDetail.findViewById<TextView>(R.id.uploadedAt)
         var back = dialogDetail.findViewById<ImageView>(R.id.back)

         var edit = dialogDetail.findViewById<Button>(R.id.edit)
         var delete = dialogDetail.findViewById<Button>(R.id.delete)

         edit.setOnClickListener()
         {
             dialogDetail.dismiss()

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
             next.setOnClickListener {
                 if (title.text.toString().isEmpty() || description.text.toString().isEmpty() ||
                     date.text.toString().isEmpty() || catagory.text.isEmpty() || priority.text.isEmpty()
                 ) {
                     Toast.makeText(this, "Please Enter All fields", Toast.LENGTH_SHORT).show()
                 } else {

                     taskModel.title = title.text.toString()
                     taskModel.description = description.text.toString()
                     taskModel.task_date = date.text.toString()
                     taskModel.Catagory = catagory.text.toString()
                     taskModel.priority = priority.text.toString()

                     update_task(taskModel)

                     dialog.dismiss()
                 }
             }


         }
         delete.setOnClickListener()
         {
             dialogDetail.dismiss()
             var builder=AlertDialog.Builder(this)
             builder.setTitle("Confirmation")
             builder.setMessage("Are you sure want to delete?")
             builder.setPositiveButton("yes"){
                     builder,which->
                 deletetask(taskModel)
             }
             builder.setNegativeButton("No"){
                     builder,which->
                 builder.dismiss()
             }
             builder.show()

         }
         back.setOnClickListener() { dialogDetail.dismiss() }

         title.setText(taskModel.title)
         description.setText(taskModel.description)
        category.setText(taskModel.Catagory)
         priority.setText(taskModel.priority)
         date.setText(taskModel.task_date)


    dialogDetail.show()
}



     private fun deletetask(taskModel: task_model) {

         db.collection("Tasks").document(taskModel.task_id).delete()
             .addOnSuccessListener{
                 Toast.makeText(this, "delete successfully", Toast.LENGTH_SHORT).show()
                 setAdaptertask()
             }
             .addOnFailureListener {
                 Toast.makeText(this, "failure", Toast.LENGTH_SHORT).show()
             }

     }
     private fun update_task(taskModel: task_model) {
         db.collection("Tasks").document(taskModel.task_id).set(taskModel)
             .addOnSuccessListener{
                 Toast.makeText(this, "Updated successfully", Toast.LENGTH_SHORT).show()
                 setAdaptertask()
             }
             .addOnFailureListener {
                 Toast.makeText(this, "Failure", Toast.LENGTH_SHORT).show()
             }
     }
     override fun onEditClick(taskModel: task_model) {

        }
     override fun onDeleteClick(taskModel: task_model) {
     }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == IMAGE_PICKER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            imageURI = data?.data
            Glide.with(this)
                .load(imageURI)
                .centerCrop()
                .placeholder(R.drawable.ic_launcher_background)
                .into(binding.iamgetodo)

        }
    }


}

