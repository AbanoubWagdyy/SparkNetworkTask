package com.sparknetwork.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView
import com.sparknetwork.R
import com.sparknetwork.model.ImageModel

class ImagesAdapter(private val moviesList: List<ImageModel.DataModel>) :
    RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: TextView = view.findViewById(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val (name) = moviesList[position]
    }

    override fun getItemCount(): Int {
        return moviesList.size
    }
}