package com.example.foodorderingapp.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.DetailsActivity
import com.example.foodorderingapp.databinding.MenuItemBinding
import com.example.foodorderingapp.model.MenuItem

//private val menuItems : List<String>, private val menuItemPrice : List<String>, private val menuItemImage : List<Int>, private val requreContext : Context
// vairable lists ki jgh model use karenge or uske liye ek model data class bna lenge
class MenuAdapter(
    private val menuItems: List<MenuItem>,
    private val requireContext: Context
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    private val itemClickListener: OnClickListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {

        val binding = MenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun getItemCount(): Int = menuItems.size

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(position)
    }

    inner class MenuViewHolder(private val binding: MenuItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    //itemClickListener?.onItemClick(position)    iski jgh hum ek function bnaenge
                    openDetailsActivity(position)
                }

            }
        }

        private fun openDetailsActivity(position: Int) {
            //jitne bhi items hmaare pass honge ..unko put krke dusri activity me bhejna h intent k through
            val menuItem = menuItems[position]
            //intent to open detail activity an d pass data
            val intent = Intent(requireContext, DetailsActivity::class.java).apply {
                putExtra("menuItemName", menuItem.foodName)
                putExtra("menuItemPrice", menuItem.foodPrice)
                putExtra("menuItemImage", menuItem.foodImage)
                putExtra("menuItemDescription", menuItem.foodDes)
                putExtra("menuItemIngredients", menuItem.foodIngredients)
            }
            //start the detail activity
            requireContext.startActivity(intent)
        }

        //set data into Recycler View (items name, price, image)
        fun bind(position: Int) {
            binding.apply {
                val menuItem = menuItems[position]
                tvFoodNameMenuItem.text = menuItem.foodName
                tvFoodPriceMenuItem.text = menuItem.foodPrice
                val uri = Uri.parse(menuItem.foodImage)
                //use glide
                Glide.with(requireContext)
                    .load(uri)
                    .into(imgMenuItem)
            }
        }
    }



}

//private fun OnClickListener?.onItemClick(position: Int) {
//
//}

