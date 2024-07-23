package com.example.foodorderingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.databinding.MenuItemBinding

class MenuAdapter( private val menuItems : List<String>, private val menuItemPrice : List<String>, private val menuItemImage : List<Int> ) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val binding =  MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding : MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(position: Int){
            binding.apply{
                tvFoodNameMenuItem.text = menuItems[position]
                tvFoodPriceMenuItem.text = menuItemPrice[position]
                imgMenuItem.setImageResource(menuItemImage[position])
            }
        }
    }
}

