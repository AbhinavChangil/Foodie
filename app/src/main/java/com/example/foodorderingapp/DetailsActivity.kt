package com.example.foodorderingapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.ActivityDetailsBinding
import com.example.foodorderingapp.model.CartItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlin.system.exitProcess

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    //varibale to get values from menu adapter
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //initialize auth
        auth = FirebaseAuth.getInstance()

        foodName = intent.getStringExtra("menuItemName")
        foodPrice = intent.getStringExtra("menuItemPrice")
        foodDescription = intent.getStringExtra("menuItemDescription")
        foodIngredients = intent.getStringExtra("menuItemIngredients")
        foodImage = intent.getStringExtra("menuItemImage")

        //ek sath bindign use krne k liye "with" lgaya h
        with(binding) {
            tvFoodNameDetails.text = foodName
            tvDesDetails.text = foodDescription
            tvIngredientDetails.text = foodIngredients
            Glide.with(this@DetailsActivity)
                .load(foodImage)
                .into(imgDetails)

        }

//        val menuFoodName = intent.getStringExtra("menuItemName")
//        val menuFoodImage = intent.getIntExtra("menuItemImage", 1)
//        binding.tvFoodNameDetails.text = menuFoodName
//        binding.imgDetails.setImageResource(menuFoodImage)


        binding.btnBackDetails.setOnClickListener {
            finish()
        }

        binding.btnAddtocartDetails.setOnClickListener {
            addItemToCart()
        }


    }

    private fun addItemToCart() {
        //isme hume ek database ka reference chahiye or user ki id chahiye taaki item particular used ki id me hi save ho
        val database = FirebaseDatabase.getInstance().reference
        val userId = auth.currentUser?.uid?:""
        //ek object create krenge jisme hmaare jitne bhi items hn unki saari details save hongi
        //A CART ITEM OBJECT and model data class jisme cart item add hoga
        val cartItem = CartItems(foodName.toString(),foodPrice.toString(),foodImage.toString(),foodDescription.toString(),1,foodIngredients.toString())

        //save data to cart item to firebase database
        database.child("user").child(userId).child("CartItems")
            .push()
            .setValue(cartItem)
            .addOnSuccessListener {
                showToast("Item added to Cart!")
                finish()
            }
            .addOnFailureListener {
                showToast("Item Not Added!")
            }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}