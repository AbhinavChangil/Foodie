package com.example.foodorderingapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.PayOutActivity
import com.example.foodorderingapp.R
import com.example.foodorderingapp.adapter.CartAdapter
import com.example.foodorderingapp.databinding.CartItemBinding
import com.example.foodorderingapp.databinding.FragmentCartBinding


class CartFragment : Fragment() {

    //sabse phle enable krenge binding ko
    private lateinit var binding: FragmentCartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCartBinding.inflate(inflater, container, false)


        //ab hum bnaenge dummy data
        val cartFoodNames = listOf("Ras Malai", "Jalebi", "Laddu", "Gulab Jamun", "Fruit Salad")
        val cartFoodPrices = listOf("Rs. 50", "Rs. 70", "Rs. 250", "Rs. 30", "Rs. 100")
        val cartFoodImages = listOf(
            R.drawable.rasmalai,
            R.drawable.jalebi,
            R.drawable.laddu,
            R.drawable.gulabjamun,
            R.drawable.menu2
        )

        val adapter = CartAdapter(
            ArrayList(cartFoodNames),
            ArrayList(cartFoodPrices),
            ArrayList(cartFoodImages)
        )
        binding.rvCart.layoutManager = LinearLayoutManager(requireContext())
        binding.rvCart.adapter = adapter

        binding.btnProceedCart.setOnClickListener{
            val intent = Intent(requireContext(), PayOutActivity::class.java)
            startActivity(intent)
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    companion object {

    }
}