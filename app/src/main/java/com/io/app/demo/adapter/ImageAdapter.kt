package com.io.app.demo.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.io.app.demo.R
import com.io.app.demo.databinding.ItemImagesBinding

class ImageAdapter(var context: Context) : RecyclerView.Adapter<ImageAdapter.ViewHolder>() {

    var mlist: ArrayList<Uri> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    inner class ViewHolder(var binding: ItemImagesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Uri) {
            Glide.with(binding.root).load(item).into(binding.ivImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ItemImagesBinding>(
            LayoutInflater.from(parent.context),
            R.layout.item_images, parent, false
        )
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = mlist.size

    fun clear() {
        mlist.clear()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(mlist[position])
    }


}