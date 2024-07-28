package com.example.foodorderingapp.Fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.example.foodorderingapp.model.MenuItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class SearchFragment : Fragment() {
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: MenuAdapter

    //variables database ko access krne k liye or ek list show krne k liye
    private lateinit var database: FirebaseDatabase
    private lateinit var searchMenuItems: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false)

        //yha hume data chaiye (phle dummy tha ab backend se retrieve krenge)
        retrieveMenuItems()


        //SHOW ALL MENU ITEMS
        showAllMenu()

        //SET UP FOR SEARCH VIEW
        setUpSearchView()




        return binding.root
    }

    private fun retrieveMenuItems() {
        // yha hume data ko recieve krna hai

        //phle database ko initialize kr lenege
        database = FirebaseDatabase.getInstance()

        val foodRef: DatabaseReference = database.reference.child("menu")
        searchMenuItems = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        searchMenuItems.add(it)
                    }
                }
                Log.d("ITEMS", "onDataChanged: Data Recieved")


//                once data recieved , set to adapter
                setAdapter(ArrayList(searchMenuItems))

            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    private fun showAllMenu() {

        val filteredMenuItems = ArrayList(searchMenuItems)

        setAdapter(filteredMenuItems)

        adapter.notifyDataSetChanged()
    }

    private fun setAdapter(filteredMenuItems: ArrayList<MenuItem>) {
        adapter = MenuAdapter(filteredMenuItems, requireContext())
        binding.rvSearch.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSearch.adapter = adapter

    }

    private fun setUpSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {

                val imm =
                    requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
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
        val filterMenuItems = searchMenuItems.filter {
            it.foodName?.contains(query,ignoreCase = true) == true
        }
        setAdapter(ArrayList(filterMenuItems))
    }

    companion object {

    }
}