package com.example.foodorderingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.example.foodorderingapp.databinding.ActivityDetailsBinding
import kotlin.system.exitProcess

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsBinding

    //varibale to get values from menu adapter
    private var foodName: String? = null
    private var foodPrice: String? = null
    private var foodImage: String? = null
    private var foodDescription: String? = null
    private var foodIngredients: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

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


    }
}