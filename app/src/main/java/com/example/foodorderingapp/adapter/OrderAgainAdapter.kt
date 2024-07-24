package com.example.foodorderingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.databinding.OrderAgainItemBinding

class OrderAgainAdapter (private val orderAgainItemName : ArrayList<String>, private val orderAgainItemPrice : ArrayList<String>, private val orderAgainItemImage : ArrayList<Int> ) : RecyclerView.Adapter<OrderAgainAdapter.OrderAgainViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderAgainViewHolder {
        val binding = OrderAgainItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return OrderAgainViewHolder(binding)
    }


    override fun onBindViewHolder(holder: OrderAgainViewHolder, position: Int) {
        holder.bind(orderAgainItemName[position],orderAgainItemPrice[position], orderAgainItemImage[position])
    }


    override fun getItemCount(): Int = orderAgainItemName.size


    class OrderAgainViewHolder(private val binding : OrderAgainItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(foodName: String, foodPrice: String, foodImage: Int) {
            binding.tvFoodNameOrderAgain.text = foodName
            binding.tvFoodPriceOrderAgain.text = foodPrice
            binding.imgOrderAgain.setImageResource(foodImage)
        }

    }


}