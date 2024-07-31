package com.example.foodorderingapp.Fragment

import android.content.Intent
import android.graphics.Color
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

    private lateinit var binding: FragmentHistoryBinding
    private lateinit var orderAgainAdapter: OrderAgainAdapter

    // Variables for getting data from order details
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
        binding = FragmentHistoryBinding.inflate(inflater, container, false)

        // Initialize auth and database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Retrieve and display the user order history
        retrieveOrderHistory()

        // Set on click listener on order item
        binding.constraintLayoutRecentOrder.setOnClickListener {
            seeItemsRecentOrder()
        }

        // Received button functioning
        binding.btnPayHistory.setOnClickListener {
            // Update payment status in CompletedOrders
            updateOrderStatus()
        }

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun updateOrderStatus() {
        if (!isAdded) return

        val itemPushKey = listOfOrderItems.getOrNull(0)?.itemPushKey
        val completedOrdersReference = itemPushKey?.let {
            database.reference.child("CompletedOrders").child(it)
        } ?: return

        completedOrdersReference.child("paymentReceived").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return

                val paymentReceived = snapshot.getValue(Boolean::class.java) ?: false
                if (paymentReceived) {
                    binding.btnPayHistory.text = "Paid"
                } else {
                    completedOrdersReference.child("paymentReceived").setValue(true).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            binding.btnPayHistory.text = "Paid"
                        } else {
                            binding.btnPayHistory.text = "Pay"
                        }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    private fun setDataInRecentOrderItem() {
        if (!isAdded) return

        binding.constraintLayoutRecentOrder.visibility = View.VISIBLE
        val recentOrderItem = listOfOrderItems.firstOrNull()
        recentOrderItem?.let {
            with(binding) {
                tvFoodNameRecentOrder.text = it.foodNamesOrderDetails?.firstOrNull() ?: ""
                tvFoodPriceRecentOrder.text = it.foodPricesOrderDetails?.firstOrNull() ?: ""
                val image = it.foodImagesOrderDetails?.firstOrNull() ?: ""
                val uri = Uri.parse(image)
                Glide.with(requireContext())
                    .load(uri)
                    .into(imgRecentOrder)

                val isOrderAccepted = it.orderAccepted
                if (isOrderAccepted) {
                    orderStatus.background.setTint(Color.parseColor("#218A3E"))
                    btnPayHistory.visibility = View.VISIBLE

                    // Check payment status
                    val itemPushKey = it.itemPushKey
                    val completedOrdersReference = itemPushKey?.let {
                        database.reference.child("CompletedOrders").child(it)
                    } ?: return

                    completedOrdersReference.child("paymentReceived").addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (!isAdded) return

                            val paymentReceived = snapshot.getValue(Boolean::class.java) ?: false
                            if (paymentReceived) {
                                btnPayHistory.text = "Paid"
                            } else {
                                btnPayHistory.text = "Pay"
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            // Handle possible errors
                        }
                    })
                }
            }
        }
    }

    private fun seeItemsRecentOrder() {
        if (!isAdded) return

        listOfOrderItems.firstOrNull()?.let { recentOrder ->
            val intent = Intent(context, RecentOrderItems::class.java)
            intent.putExtra("recentOrderItemPassed", listOfOrderItems)
            startActivity(intent)
        }
    }

    // Function to retrieve recent history item
    private fun retrieveOrderHistory() {
        if (!isAdded) return

        // Disable recent order if no order is placed
        binding.constraintLayoutRecentOrder.visibility = View.INVISIBLE
        userId = auth.currentUser?.uid ?: ""

        val orderItemReference: DatabaseReference = database.reference.child("user").child(userId).child("OrderHistory")
        val sortingQuery = orderItemReference.orderByChild("currentTime")

        sortingQuery.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (!isAdded) return

                for (orderSnapshot in snapshot.children) {
                    val orderHistoryItem = orderSnapshot.getValue(OrderDetails::class.java)
                    orderHistoryItem?.let {
                        listOfOrderItems.add(it)
                    }
                }
                listOfOrderItems.reverse()
                if (listOfOrderItems.isNotEmpty()) {
                    // Display the most recent order details
                    setDataInRecentOrderItem()
                    // Set up the recycler view with previous order details
                    setPreviousOrderItemsRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors
            }
        })
    }

    // Function to set up previous order recycler view
    private fun setPreviousOrderItemsRecyclerView() {
        if (!isAdded) return

        // Food name, price, image
        val orderAgainFoodNames = mutableListOf<String>()
        val orderAgainFoodPrices = mutableListOf<String>()
        val orderAgainFoodImages = mutableListOf<String>()
        for (i in 1 until listOfOrderItems.size) {
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
