package com.harshal.internshalatrainingsproject.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.adapter.CartRecyclerAdapter
import com.harshal.internshalatrainingsproject.adapter.DescriptionRecyclerAdapter
import com.harshal.internshalatrainingsproject.model.Dish
import com.harshal.internshalatrainingsproject.util.ConnectionManager
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import kotlin.properties.Delegates

class CartActivity : AppCompatActivity() {

    lateinit var recyclerCart : RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    lateinit var recyclerAdapter: CartRecyclerAdapter
    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var btnProceed : Button
    lateinit var btnToMain : Button
    lateinit var orderPlace : TextView
    lateinit var confirmScreen : RelativeLayout
    lateinit var sharedPreferences: SharedPreferences
    lateinit var foodArray : JSONArray
    var total : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        recyclerCart = findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(this)
        toolbar = findViewById(R.id.toolbar)
        btnProceed = findViewById(R.id.btnProceedToCart)
        btnToMain = findViewById(R.id.btnProceedToMain)
        confirmScreen = findViewById(R.id.confirmLayout)
        orderPlace = findViewById(R.id.orderRestaurant)
        foodArray = JSONArray()
        setUpToolbar()

        if(intent != null){
            val orderList = intent.getParcelableArrayListExtra<Dish>("order")
            val restaurantName = "Ordering From: " + intent.getStringExtra("resName")
            orderPlace.text = restaurantName

            for(i in orderList){
                total += i.dishPrice?.toInt() ?: 0
                val obj = JSONObject().put("food_item_id",i.dishId)
                foodArray.put(obj)
            }
            val temp = "Place Order(Total ₹$total)"
            btnProceed.text = temp

            recyclerAdapter = CartRecyclerAdapter(this, orderList)
            recyclerCart.adapter = recyclerAdapter
            recyclerCart.layoutManager = layoutManager

            btnProceed.setOnClickListener {

                val queue = Volley.newRequestQueue(this)
                val url = "http://13.235.250.119/v2/place_order/fetch_result/"
                val jsonParams = JSONObject()
                    .put("user_id",sharedPreferences.getString("user_id","0"))
                    .put("restaurant_id", orderList[0].restaurantId)
                    .put("total_cost",total)
                    .put("food",foodArray)

                if(ConnectionManager().checkConnectivity(this)){

                    val jsonObjectRequest = object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                        try {
                            val res = it.getJSONObject("data")
                            val success = res.getBoolean("success")
                            if(success){
                                confirmScreen.visibility = View.VISIBLE
                                btnProceed.visibility = View.GONE

                            } else {
                                Toast.makeText(this, "Error in placing order! Please try again.", Toast.LENGTH_SHORT).show()
                            }

                        } catch (e: JSONException){
                            Toast.makeText(this, "Some unexpected error occurred!!!", Toast.LENGTH_SHORT).show()
                        }

                    }, Response.ErrorListener {
                        Toast.makeText(this, "Volley error occurred!!!", Toast.LENGTH_SHORT).show()
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
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle("ERROR")
                    dialog.setMessage("Internet Connection Not Found")
                    dialog.setPositiveButton("Open Setting"){text, listener ->
                        val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                        startActivity(settingIntent)
                        finish()
                    }
                    dialog.setNegativeButton("Exit"){text, listener ->
                        ActivityCompat.finishAffinity(this)
                    }
                    dialog.create().show()
                }
            }

            btnToMain.setOnClickListener {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.title = "My Cart"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}