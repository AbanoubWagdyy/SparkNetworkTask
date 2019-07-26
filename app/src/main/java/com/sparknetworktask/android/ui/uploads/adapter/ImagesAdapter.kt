package com.sparknetworktask.android.ui.uploads.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.sparknetworktask.android.R
import com.sparknetworktask.android.model.ImageModel

class ImagesAdapter(private val mContext: Context?, private val imagesList: List<ImageModel>) :
    RecyclerView.Adapter<ImagesAdapter.MyViewHolder>() {

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: ImageView = view.findViewById(R.id.iv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_list_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = imagesList[position]
        Glide
            .with(mContext!!)
            .load(model.url)
            .override(200, 200)
            .centerCrop()
            .into(holder.image)
    }

    override fun getItemCount(): Int {
        return imagesList.size
    }
}