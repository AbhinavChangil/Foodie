package com.example.foodorderingapp.Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.R
import com.example.foodorderingapp.adapter.OrderAgainAdapter
import com.example.foodorderingapp.databinding.FragmentHistoryBinding


class HistoryFragment : Fragment() {

    private lateinit var binding : FragmentHistoryBinding
    private lateinit var orderAgainAdapter: OrderAgainAdapter
    private lateinit var orderAgainFoodNames : ArrayList<String>
    private lateinit var orderAgainFoodPrices : ArrayList<String>
    private lateinit var orderAgainFoodImages : ArrayList<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)



        //set up recycler view for previous orders
        setUpRecyclerView()

        //Recent Order Setup as 1st item from previous order
        binding.tvFoodNameRecentOrder.text = orderAgainFoodNames[0]
        binding.tvFoodPriceRecentOrder.text = orderAgainFoodPrices[0]
        binding.imgRecentOrder.setImageResource(orderAgainFoodImages[0])

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun setUpRecyclerView(){
        orderAgainFoodNames = arrayListOf("Ras Malai", "Jalebi", "Laddu", "Gulab Jamun", "Fruit Salad")
        orderAgainFoodPrices = arrayListOf("Rs. 50", "Rs. 70", "Rs. 250", "Rs. 30", "Rs. 100")
        orderAgainFoodImages = arrayListOf(R.drawable.rasmalai,R.drawable.jalebi, R.drawable.laddu, R.drawable.gulabjamun, R.drawable.menu2)
        orderAgainAdapter = OrderAgainAdapter(orderAgainFoodNames,orderAgainFoodPrices, orderAgainFoodImages)
        binding.rvHistory.adapter = orderAgainAdapter
        binding.rvHistory.layoutManager = LinearLayoutManager(requireContext())
    }

    companion object {

    }
}