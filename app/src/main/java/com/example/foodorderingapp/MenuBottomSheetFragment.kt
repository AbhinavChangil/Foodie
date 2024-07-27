package com.example.foodorderingapp

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.MenuAdapter
import com.example.foodorderingapp.databinding.FragmentMenuBottomSheetBinding
import com.example.foodorderingapp.model.MenuItem
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MenuBottomSheetFragment : BottomSheetDialogFragment() {
    // enable binding
    private lateinit var binding: FragmentMenuBottomSheetBinding

    //variables database ko access krne k liye or ek list show krne k liye
    private lateinit var database: FirebaseDatabase
    private lateinit var menuItems1: MutableList<MenuItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBottomSheetBinding.inflate(inflater, container, false)

        binding.btnBackMenu.setOnClickListener {
            dismiss()
        }

        //yha hume data chaiye (phle dummy tha ab backend se retrieve krenge)
        retrieveMenuItems()


        return binding.root
    }

    private fun retrieveMenuItems() {
        // yha hume data ko recieve krna hai

        //phle database ko initialize kr lenege
        database = FirebaseDatabase.getInstance()
        val foodRef: DatabaseReference = database.reference.child("menu")
        menuItems1 = mutableListOf()

        foodRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (foodSnapshot in snapshot.children) {
                    val menuItem = foodSnapshot.getValue(MenuItem::class.java)
                    menuItem?.let {
                        menuItems1.add(it)
                    }
                }
                Log.d("ITEMS","onDataChanged: Data Recieved")

                //once data recieved , set to adapter
                setAdapter()
            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
    }


    private fun setAdapter() {
        if(menuItems1.isNotEmpty()) {
            val adapter = MenuAdapter(menuItems1, requireContext())
            binding.rvMenu.layoutManager = LinearLayoutManager(requireContext())
            binding.rvMenu.adapter = adapter
            Log.d("ITEMS", "setAdapter: data set")
        }else{
            Log.d("ITEMS","setAdapter: data not set")
        }
    }

    companion object {
    }
}


// No need of dummy data as we have now integrated backend
////ab hum bnaenge dummy data
//val menuItemsFoodNames = listOf("Ras Malai", "Jalebi", "Laddu", "Gulab Jamun", "Fruit Salad", "Burger", "Herbal Pancake", "Green Noodles")
//val menuItemsFoodPrices = listOf("Rs. 50", "Rs. 70", "Rs. 250", "Rs. 30", "Rs. 100", "Rs. 70", "Rs. 110", "Rs. 150")
//val menuItemsFoodImages = listOf(
//    R.drawable.rasmalai,
//    R.drawable.jalebi,
//    R.drawable.laddu,
//    R.drawable.gulabjamun,
//    R.drawable.menu7,
//    R.drawable.menu4,
//    R.drawable.menu1,
//    R.drawable.menu2
//)