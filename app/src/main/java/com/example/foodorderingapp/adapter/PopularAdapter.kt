package com.example.foodorderingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.databinding.PopularItemBinding

class PopularAdapter ( private val items : List<String>, private val price : List<String>, private val image : List<Int>) : RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(PopularItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        val item = items[position]
        val images = image[position]
        val price = price[position]
        holder.bind(item,price,images)
    }

    class PopularViewHolder (private val binding : PopularItemBinding)  : RecyclerView.ViewHolder(binding.root) {
        private val imageView = binding.imgPopular
        fun bind(item: String, price: String, images: Int) {
            binding.tvFoodNamePopular.text = item
            binding.tvFoodPricePopular.text = price
            imageView.setImageResource(images)
        }

    }

}