package com.HISkyTech.LoginScreen.Adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.HISkyTech.LoginScreen.Models.task_model
import com.HISkyTech.LoginScreen.R
import java.text.SimpleDateFormat
import java.util.Locale

class AdapterTask (
    private val context: Context,
    private val list: List<task_model>, val listener: OnItemClickListener
) : RecyclerView.Adapter<AdapterTask.ViewHolder>() {
    interface OnItemClickListener {

        fun onItemClick(taskModel: task_model)
        fun onDeleteClick(taskModel: task_model)
        fun onEditClick(taskModel: task_model)

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.task_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val task_title: TextView = itemView.findViewById(R.id.task_name)
        private val container: CardView = itemView.findViewById(R.id.containeruser)



        init {

            container.setOnClickListener { listener.onItemClick(list[adapterPosition]) }

        }
        @SuppressLint("SuspiciousIndentation")
        fun bind(taskModel: task_model) {

            task_title.text = taskModel.title




            val dateTimeFormat = SimpleDateFormat("dd MMMM yyyy, h:mm a", Locale.getDefault())
            val formattedDateTime = dateTimeFormat.format(taskModel.createdAt.toDate())
            itemView.findViewById<TextView>(R.id.uploadedAt).text = formattedDateTime

        }

    }

    companion object {
        fun notifyDataSetChanged() {

        }
    }
}
