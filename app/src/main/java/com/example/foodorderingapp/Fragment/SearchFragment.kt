package com.example.foodorderingapp.Fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.R
import com.example.foodorderingapp.adapter.MenuAdapter
import com.example.foodorderingapp.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {
    private lateinit var binding : FragmentSearchBinding
    private lateinit var adapter: MenuAdapter
    //ab hum bnaenge dummy data
    private val originalFoodNames = listOf("Ras Malai", "Jalebi", "Laddu", "Gulab Jamun", "Fruit Salad", "Burger", "Herbal Pancake", "Green Noodles")
    private val originalFoodPrices = listOf("Rs. 50", "Rs. 70", "Rs. 250", "Rs. 30", "Rs. 100", "Rs. 70", "Rs. 110", "Rs. 150")
    private val originalFoodImages = listOf(
        R.drawable.rasmalai,
        R.drawable.jalebi,
        R.drawable.laddu,
        R.drawable.gulabjamun,
        R.drawable.menu7,
        R.drawable.menu4,
        R.drawable.menu1,
        R.drawable.menu2
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private val filteredMenuFoodNames = mutableListOf<String>()
    private val filteredMenuFoodPrices = mutableListOf<String>()
    private val filteredMenuFoodImages = mutableListOf<Int>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater,container,false)

        adapter = MenuAdapter(
            filteredMenuFoodNames,
            filteredMenuFoodPrices,
            filteredMenuFoodImages,
            requireContext()
        )
        binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearch.adapter = adapter


        //SET UP FOR SEARCH VIEW
        setUpSearchView()

        //SHOW ALL MENU ITEMS
        showAllMenu()


        return binding.root
    }

    private fun showAllMenu() {
        filteredMenuFoodNames.clear()
        filteredMenuFoodPrices.clear()
        filteredMenuFoodImages.clear()

        filteredMenuFoodNames.addAll(originalFoodNames)
        filteredMenuFoodPrices.addAll(originalFoodPrices)
        filteredMenuFoodImages.addAll(originalFoodImages)

        adapter.notifyDataSetChanged()
    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String): Boolean {

                val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.searchView.windowToken, 0)

                filterMenuItems(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                filterMenuItems(newText)
                return true
            }

        })
    }

    private fun filterMenuItems(query: String) {
        filteredMenuFoodNames.clear()
        filteredMenuFoodPrices.clear()
        filteredMenuFoodImages.clear()

        originalFoodNames.forEachIndexed{
            index, foodName ->
            if(foodName.contains(query, ignoreCase = true)){
                filteredMenuFoodNames.add(foodName)
                filteredMenuFoodPrices.add(originalFoodPrices[index])
                filteredMenuFoodImages.add(originalFoodImages[index])
            }
        }
        adapter.notifyDataSetChanged()
    }

    companion object {

    }
}