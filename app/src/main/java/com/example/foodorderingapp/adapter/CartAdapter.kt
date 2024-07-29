package com.example.foodorderingapp.adapter

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.CartItemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CartAdapter(
    private val context: Context,
    private val cartItems: MutableList<String>,
    private val cartItemPrices: MutableList<String>,
    private val cartImages: MutableList<String>,
    //saare variables create krenge jo CartItems data class me hn
    private var cartItemDescriptions: MutableList<String>,
    private val cartItemQuantity: MutableList<Int>,
    private val cartItemIngredients: MutableList<String>
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    //create and get instance firebase auth
    private val auth = FirebaseAuth.getInstance()

    init {
        //firebase initialization
        val database = FirebaseDatabase.getInstance()
        val userId = auth.currentUser?.uid ?: ""
        val cartItemNumber = cartItems.size

        itemQuantities = IntArray(cartItemNumber) { 1 }
        cartItemsReference = database.reference.child("user").child(userId).child("CartItems")
    }

    companion object {
        private var itemQuantities: IntArray = intArrayOf()
        private lateinit var cartItemsReference: DatabaseReference
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val binding = CartItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CartViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        // yaha hum data ko karenge bind
        holder.bind(position)
        // bind ko create kr lenge member function
    }

    override fun getItemCount(): Int = cartItems.size


    //get updated quantity (function is used in CartFragment.kt)
    fun getUpdatedItemQuantities() : MutableList<Int>{
        val itemQuantity = mutableListOf<Int>()
        itemQuantity.addAll(cartItemQuantity)
        return itemQuantity
    }

    //isko hum bna lenge inner class
    inner class CartViewHolder(
        //varibale to complete binding
        private val binding: CartItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {


        fun bind(position: Int) {
            binding.apply {
                val quantity = itemQuantities[position]
                tvFoodNameCartItem.text = cartItems[position]
                tvFoodPriceCartItem.text = cartItemPrices[position]
//                imgCartItem.setImageResource(cartImage[position])

                //load image using glide
                val uriString = cartImages[position]
                val uri = Uri.parse(uriString)
                Glide.with(context)
                    .load(uri)
                    .into(imgCartItem)
                tvCartItemCount.text = quantity.toString()


                btnMinusCartItem.setOnClickListener {
                    decrease(position)
                }

                btnPlusCartItem.setOnClickListener {
                    increase(position)
                }

                btnDeleteCartItem.setOnClickListener {
                    val itemPosition = adapterPosition
                    // phle check krega recycler view me iski position hai ya nahi
                    if (itemPosition != RecyclerView.NO_POSITION) {
                        delete(itemPosition)
                    }
                }
            }

        }

        private fun decrease(position: Int) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--
                cartItemQuantity[position] = itemQuantities[position]
                binding.tvCartItemCount.text = itemQuantities[position].toString()
            }
        }

        private fun increase(position: Int) {
            if (itemQuantities[position] < 10) {
                itemQuantities[position]++
                cartItemQuantity[position] = itemQuantities[position]
                binding.tvCartItemCount.text = itemQuantities[position].toString()
            }
        }

        private fun delete(position: Int) {
            val positionRetrieve = position
            getUniqueKeyAtPosition(positionRetrieve) { uniqueKey ->
                if (uniqueKey != null) {
                    removeItem(position, uniqueKey)
                }
            }

        }

        private fun removeItem(position: Int, uniqueKey: String) {
            //remove all the details of item
            if (uniqueKey != null) {
                cartItemsReference.child(uniqueKey).removeValue().addOnSuccessListener {
                    cartItems.removeAt(position)
                    cartItemPrices.removeAt(position)
                    cartImages.removeAt(position)
                    cartItemQuantity.removeAt(position)
                    cartItemDescriptions.removeAt(position)
                    cartItemIngredients.removeAt(position)
                    showToast("Item Deleted")
//                    itemQuantities[position] = itemQuantities[position + 1]

                    //update itemQuantitites
                    itemQuantities = itemQuantities.filterIndexed { index, i ->
                        index != position
                    }.toIntArray()
                    notifyItemRemoved(position)
                    notifyItemRangeChanged(position, cartItems.size)
                }.addOnFailureListener {
                    showToast("Error: Item Not Deleted")
                }
            }
        }

        private fun getUniqueKeyAtPosition(positionRetrieve: Int, onComplete: (String?) -> Unit) {
            cartItemsReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var uniqueKey: String? = null
                    //ek loop run krenge for snapshot children
                    snapshot.children.forEachIndexed { index, dataSnapshot ->
                        if (index == positionRetrieve) {
                            uniqueKey = dataSnapshot.key
                            return@forEachIndexed
                        }
                    }
                    onComplete(uniqueKey)
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

}