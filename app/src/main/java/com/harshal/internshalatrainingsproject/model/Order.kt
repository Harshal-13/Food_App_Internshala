package com.harshal.internshalatrainingsproject.model

data class Order (
    val restaurantName : String,
    val orderDate: String,
    val itemList : ArrayList<OrderItem>
)