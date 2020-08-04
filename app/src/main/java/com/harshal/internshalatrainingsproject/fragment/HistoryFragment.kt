package com.harshal.internshalatrainingsproject.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.adapter.HistoryRecyclerAdapter
import com.harshal.internshalatrainingsproject.model.Order
import com.harshal.internshalatrainingsproject.model.OrderItem
import com.harshal.internshalatrainingsproject.util.ConnectionManager
import org.json.JSONException

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HistoryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HistoryFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    lateinit var recyclerHistory : RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    var orderList = arrayListOf<Order>()
    lateinit var recyclerAdapter: HistoryRecyclerAdapter
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_history, container, false)

        sharedPreferences = activity?.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)!!
        recyclerHistory = view.findViewById(R.id.recyclerHistory)
        layoutManager = LinearLayoutManager(activity)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/orders/fetch_result/" + sharedPreferences.getString("user_id","0")

        if(ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                try {
                    progressLayout.visibility = View.GONE

                    val res = it.getJSONObject("data")
                    val success = res.getBoolean("success")
                    if(success){
                        val data = res.getJSONArray("data")
                        for( i in 0 until data.length()){
                            var itemList = arrayListOf<OrderItem>()
                            val orderJsonObject = data.getJSONObject(i)
                            val temp = orderJsonObject.getJSONArray("food_items")
                            for(j in 0 until temp.length()){
                                val tempItem = temp.getJSONObject(j)
                                val itemObject = OrderItem(
                                    tempItem.getString("name"),
                                    tempItem.getString("cost")
                                )
                                itemList.add(itemObject)
                            }
                            val orderObject = Order(
                                orderJsonObject.getString("restaurant_name"),
                                orderJsonObject.getString("order_placed_at"),
                                itemList
                            )
                            orderList.add(orderObject)
                        }
                        println("Response is")
                        println(orderList)
                        recyclerAdapter = HistoryRecyclerAdapter(activity as Context, orderList)

                        recyclerHistory.adapter = recyclerAdapter
                        recyclerHistory.layoutManager = layoutManager
//                        recyclerHome.addItemDecoration(DividerItemDecoration(recyclerHome.context, (layoutManager as LinearLayoutManager).orientation))
                    } else {
                        Toast.makeText(activity as Context, "Some Error Occurred", Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException){
                    Toast.makeText(activity as Context, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                }

            }, Response.ErrorListener {
                if(activity != null){
                    Toast.makeText(activity as Context, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
                }
            }) {
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String,String> ()
                    headers["Content-type"] = "application/json"
                    headers["token"] = "64dd3ad5877c86"
                    return headers
                }
            }
            queue.add(jsonObjectRequest)

        } else {
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("ERROR")
            dialog.setMessage("Internet Connection Not Found")
            dialog.setPositiveButton("Open Setting"){text, listener ->
                val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                startActivity(settingIntent)
                activity?.finish()
            }
            dialog.setNegativeButton("Exit"){text, listener ->
                ActivityCompat.finishAffinity(activity as Activity)
            }
            dialog.create().show()
        }

        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HistoryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HistoryFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }
}