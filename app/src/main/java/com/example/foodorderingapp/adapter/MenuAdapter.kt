package com.example.foodorderingapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.DetailsActivity
import com.example.foodorderingapp.databinding.MenuItemBinding

class MenuAdapter( private val menuItems : List<String>, private val menuItemPrice : List<String>, private val menuItemImage : List<Int>, private val requreContext : Context) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemClickListener : OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val binding =  MenuItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding : MenuItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init{
            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    itemClickListener?.onItemClick(position)
                }
                //set on click listener to open food item details
                val intent = Intent(requreContext, DetailsActivity::class.java)
                intent.putExtra("MenuItemName", menuItems.get(position))
                intent.putExtra("MenuItemImage", menuItemImage.get(position))
                requreContext.startActivity(intent)
            }
        }

        fun bind(position: Int){
            binding.apply{
                tvFoodNameMenuItem.text = menuItems[position]
                tvFoodPriceMenuItem.text = menuItemPrice[position]
                imgMenuItem.setImageResource(menuItemImage[position])


            }
        }
    }
    interface OnClickListener{
        fun onItemClick(position: Int)
    }
}

//private fun OnClickListener?.onItemClick(position: Int) {
//
//}

