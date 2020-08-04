package com.harshal.internshalatrainingsproject.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.model.Dish

class DescriptionRecyclerAdapter(val context: Context, var itemList: ArrayList<Dish>, var orderList: ArrayList<Dish>) : RecyclerView.Adapter<DescriptionRecyclerAdapter.DescriptionViewHolder>(){

    class DescriptionViewHolder(view: View): RecyclerView.ViewHolder(view){
        val dishName : TextView = view.findViewById(R.id.txtDishName)
        val dishPrice : TextView = view.findViewById(R.id.txtDishPrice)
        val dishNumber : TextView = view.findViewById(R.id.txtNumber)
        val addRemove : Button = view.findViewById(R.id.btnAddRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_description, parent,false)
        return DescriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        val dish = itemList[position]
        val tempString = (position+1).toString() + "."
        holder.dishName.text = dish.dishName
        val temp = "₹"+ dish.dishPrice
        holder.dishPrice.text = temp
        holder.dishNumber.text = tempString

        checkButton(dish, holder)

        holder.addRemove.setOnClickListener {
            if(dish in orderList){
                orderList.remove(dish)
            } else {
                orderList.add(dish)
            }
            checkButton(dish, holder)
        }

    }

    fun checkButton(dish: Dish, holder: DescriptionViewHolder){
        if(dish in orderList){
            holder.addRemove.text = "Remove"
            holder.addRemove.setBackgroundColor(Color.parseColor("#FFFFAB00"))
        } else {
            holder.addRemove.text = "Add"
            holder.addRemove.setBackgroundColor(Color.parseColor("#EE6C00"))
        }
    }
}