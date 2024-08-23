package com.example.foodorderingapp

import com.example.foodorderingapp.model.CartItems
import com.example.foodorderingapp.model.MenuItem
import com.example.foodorderingapp.model.OrderDetails
import com.example.foodorderingapp.model.UserModel

object Repo {

    fun getUsersList() = arrayListOf<UserModel>()
    fun getMenuItems() = arrayListOf<MenuItem>()
    fun getOrdersList() = arrayListOf<OrderDetails>()
    fun getCartItems() = arrayListOf<CartItems>()

}