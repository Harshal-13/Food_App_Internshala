package com.harshal.internshalatrainingsproject.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.activity.DescriptionActivity
import com.harshal.internshalatrainingsproject.database.RestaurantDatabase
import com.harshal.internshalatrainingsproject.database.RestaurantEntity
import com.harshal.internshalatrainingsproject.model.Restaurant
import com.squareup.picasso.Picasso
import java.util.ArrayList

class FavouritesRecyclerAdapter(val context: Context, var itemList : List<RestaurantEntity>) : RecyclerView.Adapter<FavouritesRecyclerAdapter.FavouritesViewHolder>()  {
    class FavouritesViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        val restaurantName : TextView = view.findViewById(R.id.txtRestaurantName)
        val restaurantPrice : TextView = view.findViewById(R.id.txtRestaurantPrice)
        val restaurantRating : TextView = view.findViewById(R.id.txtRestaurantRating)
        val restaurantImage : ImageView = view.findViewById(R.id.imgRestaurantImage)
        val restaurantHeart : ImageView = view.findViewById(R.id.imgHeart)
        val favouritesLayout : LinearLayout = view.findViewById(R.id.favouriteLayout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouritesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_favourites, parent,false)
        return FavouritesViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: FavouritesViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.restaurantName.text = restaurant.restaurantName
        holder.restaurantPrice.text = restaurant.restaurantPrice
        holder.restaurantRating.text = restaurant.restaurantRating
        Picasso.get().load(restaurant.restaurantImage).error(R.drawable.ic_launcher_foreground).into(holder.restaurantImage)

        holder.favouritesLayout.setOnClickListener {
            Toast.makeText(context , "Clicked on ${holder.restaurantName.text}", Toast.LENGTH_SHORT).show()
            val intent = Intent(context, DescriptionActivity::class.java)
            intent.putExtra("id", restaurant.restaurantId)
                .putExtra("name", restaurant.restaurantName)
                .putExtra("price", restaurant.restaurantPrice)
                .putExtra("rating", restaurant.restaurantRating)
                .putExtra("image", restaurant.restaurantImage)
            context.startActivity(intent)
        }

        val restaurantEntity = RestaurantEntity(
            restaurantId = restaurant.restaurantId,
            restaurantName = restaurant.restaurantName,
            restaurantPrice = restaurant.restaurantPrice,
            restaurantRating = restaurant.restaurantRating,
            restaurantImage = restaurant.restaurantImage
        )
        val checkFav = HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 1).execute()
        val isFav = checkFav.get()
        if(isFav) {
            holder.restaurantHeart.setImageResource(R.drawable.ic_heart)
        } else {
            holder.restaurantHeart.setImageResource(R.drawable.ic_heart_border)
        }

        holder.restaurantHeart.setOnClickListener {
            if(!HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 1).execute().get()){

                val async = HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 2).execute()
                val result = async.get()
                if(result) {
                    Toast.makeText(context,"${restaurant.restaurantName} added to favourites",Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.ic_heart)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            } else {

                val async = HomeRecyclerAdapter.DBAsyncTask(context, restaurantEntity, 3).execute()
                val result = async.get()
                if(result) {
                    Toast.makeText(context,"${restaurant.restaurantName} removed to favourites",Toast.LENGTH_SHORT).show()
                    holder.restaurantHeart.setImageResource(R.drawable.ic_heart_border)
                    notifyDataSetChanged()
                } else {
                    Toast.makeText(context,"Some error occurred",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

}