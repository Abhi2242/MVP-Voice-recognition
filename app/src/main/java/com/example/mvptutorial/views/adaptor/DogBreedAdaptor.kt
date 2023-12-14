package com.example.mvptutorial.views.adaptor

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.mvptutorial.R
import com.example.mvptutorial.model.DogBreedItem


class DogBreedAdaptor(private val dogList: List<DogBreedItem>):
    RecyclerView.Adapter<DogBreedAdaptor.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dog_breed_list_view, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.breadName.text = dogList[position].name
        holder.breadGroup.text = dogList[position].breed_group
        holder.breadOrigin.text = dogList[position].origin

        holder.itemView.setOnClickListener { view ->
//            val intent = Intent(view.context, DetailedActivity::class.java)
            Toast.makeText(view.context, dogList[position].name, Toast.LENGTH_LONG).show()
        }
    }

    override fun getItemCount(): Int {
        return dogList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val breadName: TextView = itemView.findViewById(R.id.tv_display_name)
        val breadGroup: TextView = itemView.findViewById(R.id.tv_display_group)
        val breadOrigin: TextView = itemView.findViewById(R.id.tv_display_origin)
    }
}