package com.harshal.internshalatrainingsproject.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.model.Restaurant
import java.util.ArrayList

class HomeRecyclerAdapter(val context: Context, val itemList : ArrayList<Restaurant>) : RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view : View) : RecyclerView.ViewHolder(view){
        val restaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val restaurantPrice : TextView = view.findViewById(R.id.txtRestaurantPrice)
        val restaurantRating : TextView = view.findViewById(R.id.txtRestaurantRating)
        val restaurantImage : ImageView = view.findViewById(R.id.imgRestaurantImage)
        val restaurantHeart : ImageView = view.findViewById(R.id.imgHeart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_home, parent,false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.restaurantName.text = restaurant.restaurantName
        holder.restaurantPrice.text = restaurant.restaurantPrice
        holder.restaurantRating.text = restaurant.restaurantRating
        holder.restaurantImage.setImageResource(restaurant.restaurantImage)
        if(restaurant.ifFavourite){
            holder.restaurantHeart.setImageResource(R.drawable.ic_heart)
        } else {
            holder.restaurantHeart.setImageResource(R.drawable.ic_heart_border)
        }
    }
}