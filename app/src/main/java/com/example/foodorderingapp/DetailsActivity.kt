package com.example.foodorderingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderingapp.databinding.ActivityDetailsBinding
import kotlin.system.exitProcess

class DetailsActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val menuFoodName = intent.getStringExtra("MenuItemName")
        val menuFoodImage = intent.getIntExtra("MenuItemImage", 1)
        binding.tvFoodNameDetails.text = menuFoodName
        binding.imgDetails.setImageResource(menuFoodImage)


        binding.btnBackDetails.setOnClickListener {
            finish()
        }



    }
}