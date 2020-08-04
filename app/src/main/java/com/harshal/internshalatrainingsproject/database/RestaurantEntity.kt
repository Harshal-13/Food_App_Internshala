package com.harshal.internshalatrainingsproject.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Restaurants")
data class RestaurantEntity (
    @PrimaryKey @ColumnInfo(name = "restaurant_id") val restaurantId: String,
    @ColumnInfo(name = "restaurant_name") val restaurantName : String,
    @ColumnInfo(name = "restaurant_rating") val restaurantRating: String,
    @ColumnInfo(name = "restaurant_price") val restaurantPrice: String,
    @ColumnInfo(name = "restaurant_image") val restaurantImage: String
)