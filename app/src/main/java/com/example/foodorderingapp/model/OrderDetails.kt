package com.example.foodorderingapp.model

import android.os.Parcel
import android.os.Parcelable

class OrderDetails() : Parcelable {
    var userUid: String? = null
    var userName: String? = null
    var foodNamesOrderDetails: MutableList<String>? = null
    var foodPricesOrderDetails: MutableList<String>? = null
    var foodImagesOrderDetails: MutableList<String>? = null
    var foodQuantitiesOrderDetails: MutableList<Int>? = null
    var addess: String? = null
    var totalPrice: String? = null
    var phoneNumber: String? = null
    var orderAccepted: Boolean = false
    var paymentRecieved: Boolean =  false
    var itemPushKey: String? = null
    var currentTime: Long = 0

    constructor(parcel: Parcel) : this() {
        userUid = parcel.readString()
        userName = parcel.readString()
        addess = parcel.readString()
        totalPrice = parcel.readString()
        phoneNumber = parcel.readString()
        orderAccepted = parcel.readByte() != 0.toByte()
        paymentRecieved = parcel.readByte() != 0.toByte()
        itemPushKey = parcel.readString()
        currentTime = parcel.readLong()
    }

    //this constructor need to be set up in order to save all order details to databse
    constructor(
        userId: String,
        userName: String,
        payoutFoodName: ArrayList<String>,
        payoutFoodPrice: ArrayList<String>,
        payoutFoodImage: ArrayList<String>,
        payoutFoodQuantity: ArrayList<Int>,
        userAddress: String,
        totalAmount: String,
        userPhone: String,
        b: Boolean,
        b1: Boolean,
        itemPushKey: String?,
        time: Long
    ) : this(){
        this.userUid = userId
        this.userName = userName
        this.foodNamesOrderDetails = payoutFoodName
        this.foodPricesOrderDetails = payoutFoodPrice
        this.foodImagesOrderDetails = payoutFoodImage
        this.foodQuantitiesOrderDetails = payoutFoodQuantity
        this.addess = userAddress
        this.totalPrice = totalAmount
        this.phoneNumber = userPhone
        this.orderAccepted = b
        this.paymentRecieved = b1
        this.itemPushKey = itemPushKey
        this.currentTime = time
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userUid)
        parcel.writeString(userName)
        parcel.writeString(addess)
        parcel.writeString(totalPrice)
        parcel.writeString(phoneNumber)
        parcel.writeByte(if (orderAccepted) 1 else 0)
        parcel.writeByte(if (paymentRecieved) 1 else 0)
        parcel.writeString(itemPushKey)
        parcel.writeLong(currentTime)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<OrderDetails> {
        override fun createFromParcel(parcel: Parcel): OrderDetails {
            return OrderDetails(parcel)
        }

        override fun newArray(size: Int): Array<OrderDetails?> {
            return arrayOfNulls(size)
        }
    }
}