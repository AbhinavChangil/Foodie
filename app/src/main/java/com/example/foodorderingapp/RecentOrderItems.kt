package com.example.foodorderingapp

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.RecentOrderAdapter
import com.example.foodorderingapp.databinding.ActivityRecentOrderItemsBinding
import com.example.foodorderingapp.model.OrderDetails

class RecentOrderItems : AppCompatActivity() {
    private val binding: ActivityRecentOrderItemsBinding by lazy {
        ActivityRecentOrderItemsBinding.inflate(layoutInflater)
    }

    private lateinit var allRecentFoodNames: ArrayList<String>
    private lateinit var allRecentFoodPrices: ArrayList<String>
    private lateinit var allRecentFoodImages: ArrayList<String>
    private lateinit var allRecentFoodQuantities: ArrayList<Int>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnBackRecentOrderItem.setOnClickListener {
            finish()
        }

        val recentOrderItemsRecieved = intent.getSerializableExtra("recentOrderItemPassed") as ArrayList<OrderDetails>
        val recentOrderItemDetails = recentOrderItemsRecieved.firstOrNull()
        recentOrderItemsRecieved?.let { orders ->
            if (orders.isNotEmpty()) {
                val recentOrderItem = orders.firstOrNull()

                allRecentFoodNames = recentOrderItem?.foodNamesOrderDetails as ArrayList<String>
                allRecentFoodPrices = recentOrderItem.foodPricesOrderDetails as ArrayList<String>
                allRecentFoodImages = recentOrderItem.foodImagesOrderDetails as ArrayList<String>
                allRecentFoodQuantities = recentOrderItem.foodQuantitiesOrderDetails as ArrayList<Int>
            }

        }

        setAdapter()


    }

    private fun setAdapter() {
        val rv = binding.rvRecentOrderItem
        rv.layoutManager = LinearLayoutManager(this)
        val adapter = RecentOrderAdapter(this,allRecentFoodNames,allRecentFoodPrices,allRecentFoodImages,allRecentFoodQuantities)
        rv.adapter = adapter
    }
}