package com.example.foodorderingapp.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.foodorderingapp.R
import com.example.foodorderingapp.RecentOrderItems
import com.example.foodorderingapp.adapter.OrderAgainAdapter
import com.example.foodorderingapp.databinding.FragmentHistoryBinding
import com.example.foodorderingapp.model.OrderDetails
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class HistoryFragment : Fragment() {

    private lateinit var binding : FragmentHistoryBinding
    private lateinit var orderAgainAdapter: OrderAgainAdapter

    //variables for getting data from order details
    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var userId: String
    private var listOfOrderItems: ArrayList<OrderDetails> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHistoryBinding.inflate(layoutInflater, container, false)

        //initialize auth and database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        //retrieve and display the user order history
        retrieveOrderHistory()

        //set on click listner on order item
        binding.constraintLayoutRecentOrder.setOnClickListener {
            seeItemsRecentOrder()
        }


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun seeItemsRecentOrder() {
        listOfOrderItems.firstOrNull()?.let{ recentOrder ->
            val intent = Intent(requireContext(), RecentOrderItems::class.java)
            intent.putExtra("recentOrderItemPassed", listOfOrderItems)
            startActivity(intent)
        }
    }

    //function to retrieve recent history item
    private fun retrieveOrderHistory() {
        //disable recent order if no order is placed
        binding.constraintLayoutRecentOrder.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid?:""


        val orderItemReference : DatabaseReference = database.reference.child("user").child(userId).child("OrderHistory")
        val sortingQuery = orderItemReference.orderByChild("currentTime")

        sortingQuery.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (orderSnapshot in snapshot.children){
                    val orderHistoryItem = orderSnapshot.getValue(OrderDetails::class.java)
                    orderHistoryItem?.let {
                        listOfOrderItems.add(it)
                    }
                }
                listOfOrderItems.reverse()
                if(listOfOrderItems.isNotEmpty()){
                    //display the most recent order details
                    setDataInRecentOrderItem()
                    //set up the recycler view with previous order details
                    setPreviousOrderItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun setDataInRecentOrderItem() {
        binding.constraintLayoutRecentOrder.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItems.firstOrNull()
        recentOrderItem?.let {
            with(binding){
                tvFoodNameRecentOrder.text = it.foodNamesOrderDetails?.firstOrNull()?:""
                tvFoodPriceRecentOrder.text = it.foodPricesOrderDetails?.firstOrNull()?:""
                //image kko glide se show kraenge
                val image = it.foodImagesOrderDetails?.firstOrNull()?:""
                val uri = Uri.parse(image)
                Glide.with(requireContext())
                    .load(uri)
                    .into(imgRecentOrder)


//                listOfOrderItems.reverse()
//                if(listOfOrderItems.isNotEmpty()){
//                    //
//                }

            }
        }
    }

    //function to setup previous order recycler view
    private fun setPreviousOrderItemsRecyclerView() {
        //food name, price, image
        val orderAgainFoodNames = mutableListOf<String>()
        val orderAgainFoodPrices = mutableListOf<String>()
        val orderAgainFoodImages = mutableListOf<String>()
        for(i in 1 until listOfOrderItems.size){
            listOfOrderItems[i].foodNamesOrderDetails?.firstOrNull()?.let { orderAgainFoodNames.add(it) }
            listOfOrderItems[i].foodPricesOrderDetails?.firstOrNull()?.let { orderAgainFoodPrices.add(it) }
            listOfOrderItems[i].foodImagesOrderDetails?.firstOrNull()?.let { orderAgainFoodImages.add(it) }
        }
        val rv = binding.rvHistory
        rv.layoutManager = LinearLayoutManager(requireContext())
        orderAgainAdapter = OrderAgainAdapter(
            orderAgainFoodNames,
            orderAgainFoodPrices,
            orderAgainFoodImages,
            requireContext()
        )
        rv.adapter = orderAgainAdapter
    }

}