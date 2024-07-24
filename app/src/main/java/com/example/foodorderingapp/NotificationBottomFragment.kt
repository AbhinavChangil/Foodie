package com.example.foodorderingapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foodorderingapp.adapter.NotificationAdapter
import com.example.foodorderingapp.databinding.FragmentNotificationBottomBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class NotificationBottomFragment : BottomSheetDialogFragment() {
        private lateinit var binding : FragmentNotificationBottomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNotificationBottomBinding.inflate(layoutInflater,container,false)

        binding.btnBackNotification.setOnClickListener {
            dismiss()
        }

        val notifications = listOf("Your order has been Cancelled Successfully", "Order has been taken by the driver", "Congrats! Your Order Placed")
        val notificationImages = listOf(R.drawable.sademoji, R.drawable.truck, R.drawable.congrats)

        val adapter = NotificationAdapter(
            ArrayList(notifications),
            ArrayList(notificationImages)
        )

        binding.rvNotification.layoutManager = LinearLayoutManager(requireContext())
        binding.rvNotification.adapter = adapter


        return binding.root
    }

    companion object {

    }
}