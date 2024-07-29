package com.example.foodorderingapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.RecentOrderItemBinding

class RecentOrderAdapter(
    private var context: Context,
    private var recentFoodNameList: ArrayList<String>,
    private var recentFoodPriceList: ArrayList<String>,
    private var recentFoodImageList: ArrayList<String>,
    private var recentFoodQuantityList: ArrayList<Int>
) : RecyclerView.Adapter<RecentOrderAdapter.RecentOrderViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentOrderViewHolder {
        val binding =
            RecentOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentOrderViewHolder(binding)
    }

    override fun getItemCount(): Int = recentFoodNameList.size

    override fun onBindViewHolder(holder: RecentOrderViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class RecentOrderViewHolder(private val binding: RecentOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int) {
            binding.apply {
                tvFoodNameRecentOrderItem.text = recentFoodNameList[position]
                tvFoodPriceRecentOrderItem.text = recentFoodPriceList[position]
                tvQuantityNumberRecentOrderItem.text = recentFoodQuantityList[position].toString()
                val uriString = recentFoodImageList[position]
                val uri = Uri.parse(uriString)
                Glide.with(context)
                    .load(uri)
                    .into(imgRecentOrderItem)
            }
        }

    }
}