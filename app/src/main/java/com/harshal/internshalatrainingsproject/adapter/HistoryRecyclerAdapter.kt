package com.harshal.internshalatrainingsproject.adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.model.Order

class HistoryRecyclerAdapter(context: Context, var orderList : ArrayList<Order>) : RecyclerView.Adapter<HistoryRecyclerAdapter.HistoryViewHolder>() {
    var context = context

    class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val restaurantName :TextView = view.findViewById(R.id.txtRestaurantName)
        val orderData : TextView = view.findViewById(R.id.txtDate)
        val recyclerHistory : RecyclerView = view.findViewById(R.id.recyclerHistory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_history, parent,false)
        return HistoryViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val order = orderList[position]
        holder.restaurantName.text = order.restaurantName
        holder.orderData.text = order.orderDate


        var recyclerAdapter = OrderItemRecyclerAdapter(context, order.itemList)
        val layoutManager = LinearLayoutManager(context)

        holder.recyclerHistory.adapter = recyclerAdapter
        holder.recyclerHistory.layoutManager = layoutManager
    }
}