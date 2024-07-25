package com.example.foodorderingapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.foodorderingapp.databinding.CartItemBinding

class CartAdapter( private val cartItems:MutableList<String>, private val cartItemPrice:MutableList<String>, private val cartImage:MutableList<Int>)  : RecyclerView.Adapter<CartAdapter.CartViewHolder>(){

    private val itemQuantities = IntArray(cartItems.size){1}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        // yaha hum data ko karenge bind
        holder.bind(position)
        // bind ko create kr lenge member function
    }

    override fun getItemCount(): Int = cartItems.size

    //isko hum bna lenge inner class
    inner class CartViewHolder(
        //varibale to complete binding
        private val binding : CartItemBinding) : RecyclerView.ViewHolder(binding.root) {


        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                tvFoodNameCartItem.text = cartItems[position]
                tvFoodPriceCartItem.text = cartItemPrice[position]
                imgCartItem.setImageResource(cartImage[position])
                tvCartItemCount.text = quantity.toString()


                btnMinusCartItem.setOnClickListener{
                    decrease(position)
                }

                btnPlusCartItem.setOnClickListener {
                    increase(position)
                }

                btnDeleteCartItem.setOnClickListener {
                    val itemPosition = adapterPosition
                    // phle check krega recycler view me iski position hai ya nahi
                    if(itemPosition != RecyclerView.NO_POSITION) {
                        delete(itemPosition)
                    }
                }
            }

        }

        private fun decrease(position : Int){
            if(itemQuantities[position]>1){
                itemQuantities[position]--
                binding.tvCartItemCount.text = itemQuantities[position].toString()
            }
        }

        private fun increase(position : Int){
            if(itemQuantities[position]<10){
                itemQuantities[position]++
                binding.tvCartItemCount.text = itemQuantities[position].toString()
            }
        }

        private fun delete(position : Int){
            cartItems.removeAt(position)
            cartItemPrice.removeAt(position)
            cartImage.removeAt(position)
            itemQuantities[position] = itemQuantities[position+1]
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, cartItems.size)
        }


    }
}