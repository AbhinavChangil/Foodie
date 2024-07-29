package com.example.foodorderingapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.DetailsActivity
import com.example.foodorderingapp.databinding.OrderAgainItemBinding

class OrderAgainAdapter (private val orderAgainItemName : MutableList<String>, private val orderAgainItemPrice : MutableList<String>, private val orderAgainItemImage : MutableList<String>, private val requireContext:Context ) : RecyclerView.Adapter<OrderAgainAdapter.OrderAgainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAgainViewHolder {
        val binding = OrderAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderAgainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OrderAgainViewHolder, position: Int) {
        holder.bind(orderAgainItemName[position],orderAgainItemPrice[position], orderAgainItemImage[position])

        holder.itemView.setOnClickListener {
            //set on click listener to open food item details
            val intent = Intent(requireContext, DetailsActivity::class.java)
            intent.putExtra("MenuItemName", orderAgainItemName.get(position))
            intent.putExtra("MenuItemImage", orderAgainItemImage.get(position))
            requireContext.startActivity(intent)
        }
    }


    override fun getItemCount(): Int = orderAgainItemName.size


    inner class OrderAgainViewHolder(private val binding : OrderAgainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String, foodPrice: String, foodImage: String) {
            binding.tvFoodNameOrderAgain.text = foodName
            binding.tvFoodPriceOrderAgain.text = foodPrice
            val uriString = foodImage
            val uri = Uri.parse(uriString)
            Glide.with(requireContext)
                .load(uri)
                .into(binding.imgOrderAgain)
        }

    }


}