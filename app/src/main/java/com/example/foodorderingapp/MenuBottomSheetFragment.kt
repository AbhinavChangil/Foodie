package com.example.foodorderingapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.MenuAdapter
import com.example.foodorderingapp.databinding.FragmentMenuBottomSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    // enable binding
    private lateinit var binding : FragmentMenuBottomSheetBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container,false)

        binding.btnBackMenu.setOnClickListener {
            dismiss()
        }

        //ab hum bnaenge dummy data
        val menuItemsFoodNames = listOf("Ras Malai", "Jalebi", "Laddu", "Gulab Jamun", "Fruit Salad", "Burger", "Herbal Pancake", "Green Noodles")
        val menuItemsFoodPrices = listOf("Rs. 50", "Rs. 70", "Rs. 250", "Rs. 30", "Rs. 100", "Rs. 70", "Rs. 110", "Rs. 150")
        val menuItemsFoodImages = listOf(
            R.drawable.rasmalai,
            R.drawable.jalebi,
            R.drawable.laddu,
            R.drawable.gulabjamun,
            R.drawable.menu7,
            R.drawable.menu4,
            R.drawable.menu1,
            R.drawable.menu2
        )

        val adapter = MenuAdapter(
            ArrayList(menuItemsFoodNames),
            ArrayList(menuItemsFoodPrices),
            ArrayList(menuItemsFoodImages)
        )
        binding.rvMenu.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMenu.adapter = adapter

        return binding.root
    }

    companion object {
    }
}