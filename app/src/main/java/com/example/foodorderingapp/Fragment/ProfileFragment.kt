package com.example.foodorderingapp.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.foodorderingapp.R
import com.example.foodorderingapp.databinding.FragmentProfileBinding
import com.example.foodorderingapp.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var binding: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        // We should not change email
        binding.edtEmailProfile.isEnabled = false

        // Enable or Disable editing
        fun enableDisable(enableDisable: Boolean) {
            binding.edtNameProfile.isEnabled = enableDisable
            binding.edtAddressProfile.isEnabled = enableDisable
            binding.edtPhoneProfile.isEnabled = enableDisable
        }

        // By default disable editing
        enableDisable(false)

        // Enable editing on edit profile clicked
        binding.editProfileLink.setOnClickListener {
            enableDisable(true)
            // When edit profile clicked focus on name edit text
            binding.edtNameProfile.requestFocus()
            binding.edtNameProfile.setSelection(binding.edtNameProfile.text.length)
        }

        // Initialize auth and database
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setUserData()

        binding.btnSaveInfoProfile.setOnClickListener {
            val name = binding.edtNameProfile.text.toString()
            val address = binding.edtAddressProfile.text.toString()
            val email = binding.edtEmailProfile.text.toString()
            val phone = binding.edtPhoneProfile.text.toString()

            updateUserData(name, address, email, phone)
            enableDisable(false)
        }

        return binding.root
    }

    private fun updateUserData(name: String, address: String, email: String, phone: String) {
        val userId = auth.currentUser?.uid?:""
        if (userId != null) {
            val userReference = database.getReference("user").child(userId).child("profile")

            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                showToast("Profile Updated Successfully!")
            }
                .addOnFailureListener {
                    showToast("Profile Update Failed!")
                }
        }
    }

    private fun setUserData() {
        val user = auth.currentUser
        if (user != null) {
            val userId = auth.currentUser?.uid?:""
            val userReference = database.getReference("user").child(userId).child("profile")

            // Set all values
            userReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if (userProfile != null) {
                            binding.apply {
                                edtNameProfile.setText(userProfile.name)
                                edtAddressProfile.setText(userProfile.address)
                                edtPhoneProfile.setText(userProfile.phone)
                                edtEmailProfile.setText(userProfile.email)
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    showToast("Failed to load user data")
                }
            })
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
