package com.example.foodorderingapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.PayOutActivity
import com.example.foodorderingapp.R
import com.example.foodorderingapp.adapter.CartAdapter
import com.example.foodorderingapp.databinding.CartItemBinding
import com.example.foodorderingapp.databinding.FragmentCartBinding
import com.example.foodorderingapp.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue


class CartFragment : Fragment() {

    //sabse phle enable krenge binding ko
    private lateinit var binding: FragmentCartBinding

    //variables to update cart fragment
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var foodNames: MutableList<String>
    private lateinit var foodPrices: MutableList<String>
    private lateinit var foodImageUri: MutableList<String>
    private lateinit var foodIngredients: MutableList<String>
    private lateinit var foodDescriptions: MutableList<String>
    private lateinit var quantity: MutableList<Int>
    private lateinit var cartAdapter: CartAdapter
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)

        //auth  get instance
        auth = FirebaseAuth.getInstance()
        retrieveCartItems()


        binding.btnProceedCart.setOnClickListener {
            //get order items details before proceeding to check out
            getOrderItemsDetail()

        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun getOrderItemsDetail() {
        val orderIdReference: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")

        val foodName = mutableListOf<String>()
        val foodPrice = mutableListOf<String>()
        val foodImage = mutableListOf<String>()
        val foodDescription = mutableListOf<String>()
        val foodIngredients = mutableListOf<String>()

        //get items Quantities
        val foodQuantities = cartAdapter.getUpdatedItemQuantities()

        // details ko list me add kr lenge
        orderIdReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                for (foodSnapshot in snapshot.children) {
                    //get the cart item to repective list
                    val orderItems = foodSnapshot.getValue(CartItems::class.java)
                    // add items details into list
                    orderItems?.foodName?.let { foodName.add(it) }
                    orderItems?.foodPrice?.let { foodPrice.add(it) }
                    orderItems?.foodImage?.let { foodImage.add(it) }
                    orderItems?.foodDes?.let { foodDescription.add(it) }
                    orderItems?.foodIngredient?.let { foodIngredients.add(it) }
                }

                orderNow(
                    foodName,
                    foodPrice,
                    foodImage,
                    foodDescription,
                    foodIngredients,
                    foodQuantities
                )
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Order making Failed! Please Try Again!")
            }

        })
    }

    private fun orderNow(
        foodName: MutableList<String>,
        foodPrice: MutableList<String>,
        foodImage: MutableList<String>,
        foodDescription: MutableList<String>,
        foodIngredients: MutableList<String>,
        foodQuantities: MutableList<Int>
    ) {
        //jitne bhi item get kiye hn unko store kraennge or payout activity pr le jaenge
        if (isAdded && context != null) {
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            intent.putExtra("payoutFoodName", foodName as ArrayList<String>)
            intent.putExtra("payoutFoodPrice", foodPrice as ArrayList<String>)
            intent.putExtra("payoutFoodImage", foodImage as ArrayList<String>)
            intent.putExtra("payoutFoodDescription", foodDescription as ArrayList<String>)
            intent.putExtra("payoutFoodIngredients", foodIngredients as ArrayList<String>)
            intent.putExtra("payoutFoodQuantity", foodQuantities as ArrayList<Int>)
            startActivity(intent)
        }
    }

    private fun retrieveCartItems() {
        //database ka reference lenge
        database = FirebaseDatabase.getInstance()
        userId = auth.currentUser?.uid ?: ""

        val foodReference: DatabaseReference =
            database.reference.child("user").child(userId).child("CartItems")

        //ek list banemge jisme saare variable ko start kraenge
        foodNames = mutableListOf()
        foodPrices = mutableListOf()
        foodImageUri = mutableListOf()
        foodIngredients = mutableListOf()
        foodDescriptions = mutableListOf()
        quantity = mutableListOf()

        //data ko store kraenge in variable lists me(fetch data from database)
        foodReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    //get the cartItems object from child node
                    val cartItems = foodSnapshot.getValue(CartItems::class.java)

                    //add cart item details to the list
                    cartItems?.foodName?.let { foodNames.add(it) }
                    cartItems?.foodPrice?.let { foodPrices.add(it) }
                    cartItems?.foodImage?.let { foodImageUri.add(it) }
                    cartItems?.foodDes?.let { foodDescriptions.add(it) }
                    cartItems?.foodQuantity?.let { quantity.add(it) }
                    cartItems?.foodIngredient?.let { foodIngredients.add(it) }
                }

                setAdapter()
            }

            private fun setAdapter() {
                cartAdapter = CartAdapter(
                    requireContext(),
                    foodNames,
                    foodPrices,
                    foodImageUri,
                    foodDescriptions,
                    quantity,
                    foodIngredients
                )
                binding.rvCart.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                binding.rvCart.adapter = cartAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                showToast("Data Not Fetched!")
            }

        })
    }

    companion object {

    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}