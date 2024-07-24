package com.example.foodorderingapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.foodorderingapp.Fragment.CartFragment
import com.example.foodorderingapp.databinding.ActivityPayOutBinding

class PayOutActivity : AppCompatActivity() {
    private lateinit var binding : ActivityPayOutBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityPayOutBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.btnBackPayout.setOnClickListener {
//            dismiss()
//        }
        binding.btnPlaceOrderPayout.setOnClickListener{
            val bottomSheetDialogFragment = CongratsBottomSheetFragment()
            bottomSheetDialogFragment.show(supportFragmentManager, "test")

        }
    }
}