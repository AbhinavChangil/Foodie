package com.example.foodorderingapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderingapp.Fragment.CartFragment
import com.example.foodorderingapp.databinding.ActivityPayOutBinding
import com.example.foodorderingapp.model.OrderDetails
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlin.time.times

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPayOutBinding

    //variables to get user details from firebase
    private lateinit var auth: FirebaseAuth
    private lateinit var userName: String
    private lateinit var userAddress: String
    private lateinit var userPhone: String
    private lateinit var totalAmount: String

    //variables to get details from cart fragment via intent
    private lateinit var payoutFoodNames: ArrayList<String>
    private lateinit var payoutFoodPrices: ArrayList<String>
    private lateinit var payoutFoodImages: ArrayList<String>
    private lateinit var payoutFoodDescriptions: ArrayList<String>
    private lateinit var payoutFoodIngredients: ArrayList<String>
    private lateinit var payoutFoodQuantities: ArrayList<Int>

    //variables for database reference and userId
    private lateinit var databaseReference: DatabaseReference
    private lateinit var userId: String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Back Button
        binding.btnBackPayout.setOnClickListener {
            finish()
        }

        //Initialize auth and databse variables
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference

        //set User Data
        setUserData()

        //get user details from firebase
        val intent = intent
        payoutFoodNames = intent.getStringArrayListExtra("payoutFoodName") as ArrayList<String>
        payoutFoodPrices = intent.getStringArrayListExtra("payoutFoodPrice") as ArrayList<String>
        payoutFoodImages = intent.getStringArrayListExtra("payoutFoodImage") as ArrayList<String>
        payoutFoodDescriptions =
            intent.getStringArrayListExtra("payoutFoodDescription") as ArrayList<String>
        payoutFoodIngredients =
            intent.getStringArrayListExtra("payoutFoodIngredients") as ArrayList<String>
        payoutFoodQuantities = intent.getIntegerArrayListExtra("payoutFoodQuantity") as ArrayList<Int>



        totalAmount = calculateTotalAmount().toString()
        binding.tvTotalAmountPayout.setText("₹" + totalAmount)


        // Place Order Button
        binding.btnPlaceOrderPayout.setOnClickListener {

            //order krne k baad data send krenge history me or admin side
            //so lets get user data fron edit texts
            userName = binding.edtNamePayout.text.toString().trim()
            userAddress = binding.edtAddressPayout.text.toString().trim()
            userPhone = binding.edtPhonePayout.text.toString().trim()

            if (userName.isBlank() || userAddress.isBlank() || userPhone.isBlank()) {
                showToast("Please fill all details!")
            } else {
                placeOrder()
            }

        }


    }

    private fun placeOrder() {
        userId = auth.currentUser?.uid ?: ""

        //we need order details, time, reference here
        val time = System.currentTimeMillis()
        //ek new orderDetails k folder me order details save krenge
        val itemPushKey = databaseReference.child("OrderDetails").push().key
        val orderDetails = OrderDetails(
            userId, userName,
            payoutFoodNames, payoutFoodPrices, payoutFoodImages, payoutFoodQuantities,
            userAddress, totalAmount, userPhone,
            false, false,
            itemPushKey, time
        )

        //ek variable bnaenge jo order reference ko lega
        val orderReference = databaseReference.child("OrderDetails").child(itemPushKey!!)
        orderReference.setValue(orderDetails)
            .addOnSuccessListener {
                val bottomSheetDialogFragment = CongratsBottomSheetFragment()
                bottomSheetDialogFragment.show(supportFragmentManager, "test1")

                //also remove item from cart if orderdetails are saved
                removeItemsFromCart()

                //add order to history
                addOrderToHistory(orderDetails)

            }
    }

    private fun addOrderToHistory(orderDetails: OrderDetails) {
        databaseReference.child("user").child(userId).child("OrderHistory")
            .child(orderDetails.itemPushKey!!)
            .setValue(orderDetails)
            .addOnSuccessListener {
                showToast("Success! order added to history")
            }
            .addOnFailureListener {
                showToast("Failed to add order to history!")
            }
    }

    private fun removeItemsFromCart() {
        val cartItemsReference = databaseReference.child("user").child(userId).child("CartItems")
        cartItemsReference.removeValue()
        showToast("Items Removed from Cart!")
    }


    private fun calculateTotalAmount(): Int {
        var totalPayAmount = 0
        for (i in 0 until payoutFoodPrices.size) {
            val price = payoutFoodPrices[i]
            val firstChar = price.first()
            val priceIntValue = if (firstChar == '₹') {
                price.drop(1).trim().toInt()
            } else {
                price.trim().toInt()
            }
            var quantity = payoutFoodQuantities[i]

            totalPayAmount += priceIntValue * quantity
        }
        return totalPayAmount
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = auth.currentUser?.uid ?: ""
            val userReference = databaseReference.child("user").child(userId).child("profile")

            //set all values
            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val name = snapshot.child("name").getValue(String::class.java) ?: ""
                        val address = snapshot.child("address").getValue(String::class.java) ?: ""
                        val phone = snapshot.child("phone").getValue(String::class.java) ?: ""
                        binding.apply {
                            edtNamePayout.setText(name)
                            edtAddressPayout.setText(address)
                            edtPhonePayout.setText(phone)
                        }
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Failed to load User Data!")
                }

            })
        }

    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}