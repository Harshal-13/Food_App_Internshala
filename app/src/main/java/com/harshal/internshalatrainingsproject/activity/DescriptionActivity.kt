package com.harshal.internshalatrainingsproject.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.adapter.DescriptionRecyclerAdapter
import com.harshal.internshalatrainingsproject.adapter.HomeRecyclerAdapter
import com.harshal.internshalatrainingsproject.database.RestaurantEntity
import com.harshal.internshalatrainingsproject.model.Dish
import com.harshal.internshalatrainingsproject.util.ConnectionManager
import kotlinx.android.synthetic.main.activity_description.*
import org.json.JSONException

class DescriptionActivity : AppCompatActivity() {

    var restaurantId: String? = "0"
    var restaurantName: String? = "0"
    lateinit var recyclerDescription : RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    var dishList = arrayListOf<Dish>()
    var orderList = arrayListOf<Dish>()
    lateinit var recyclerAdapter: DescriptionRecyclerAdapter
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar: ProgressBar
    lateinit var restaurantEntity: RestaurantEntity

    lateinit var toolbar : androidx.appcompat.widget.Toolbar
    lateinit var imgHeart : ImageView
    lateinit var btnProceed : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        recyclerDescription = findViewById(R.id.recyclerDescription)
        layoutManager = LinearLayoutManager(this)
        progressLayout = findViewById(R.id.progressLayout)
        progressBar = findViewById(R.id.progressBar)

        toolbar = findViewById(R.id.toolbar)
        imgHeart = findViewById(R.id.imgHeart)
        btnProceed = findViewById(R.id.btnProceedToCart)
        setUpToolbar()

        if(intent != null){
            restaurantId = intent.getStringExtra("id")
            restaurantName = intent.getStringExtra("name")
            toolbar.title = intent.getStringExtra("name")
            restaurantEntity = RestaurantEntity(
                restaurantId = intent.getStringExtra("id")!!,
                restaurantName = intent.getStringExtra("name")!!,
                restaurantPrice = intent.getStringExtra("price")!!,
                restaurantRating = intent.getStringExtra("rating")!!,
                restaurantImage = intent.getStringExtra("image")!!
            )

        } else {
            finish()
            Toast.makeText(this, "Some unexpected error occurred!!!",Toast.LENGTH_SHORT).show()
        }
        if(restaurantId == "0"){
            finish()
            Toast.makeText(this, "Some unexpected error occurred!!!",Toast.LENGTH_SHORT).show()
        }

        putImage()
        imgHeart.setOnClickListener {
            clickHeart()
        }

        val queue = Volley.newRequestQueue(this)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/$restaurantId/"

        if(ConnectionManager().checkConnectivity(this)){

            val jsonObjectRequest = object : JsonObjectRequest(Method.GET, url, null, Response.Listener {

                try {
                    progressLayout.visibility = View.GONE

                    val res = it.getJSONObject("data")
                    val success = res.getBoolean("success")
                    if(success){
                        val data = res.getJSONArray("data")
                        for( i in 0 until data.length()){
                            val dishJsonObject = data.getJSONObject(i)
                            val dishObject = Dish(
                                dishJsonObject.getString("id"),
                                dishJsonObject.getString("name"),
                                dishJsonObject.getString("cost_for_one"),
                                dishJsonObject.getString("restaurant_id")
                            )
                            dishList.add(dishObject)
                        }
                        recyclerAdapter = DescriptionRecyclerAdapter(this, dishList, orderList)

                        recyclerDescription.adapter = recyclerAdapter
                        recyclerDescription.layoutManager = layoutManager
//                        recyclerDescription.addItemDecoration(DividerItemDecoration(recyclerDescription.context, layoutManager.orientation))
                    } else {
                        Toast.makeText(this, "Some Error Occurred", Toast.LENGTH_SHORT).show()
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

        btnProceed.setOnClickListener {
            if(orderList.size > 0){
                val intent = Intent(this, CartActivity::class.java)
                intent.putParcelableArrayListExtra("order", orderList).putExtra("resName",restaurantName)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Select atleast one dish on menu!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun setUpToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun putImage(){
        val checkFav = HomeRecyclerAdapter.DBAsyncTask(this, restaurantEntity, 1).execute()
        val isFav = checkFav.get()
        if(isFav) {
            imgHeart.setImageResource(R.drawable.ic_heart)
        } else {
            imgHeart.setImageResource(R.drawable.ic_heart_border)
        }
    }

    fun clickHeart(){
        if(!HomeRecyclerAdapter.DBAsyncTask(this, restaurantEntity, 1).execute().get()){
            val result = HomeRecyclerAdapter.DBAsyncTask(this, restaurantEntity, 2).execute().get()
            if(result) {
                Toast.makeText(this,"Added to favourites",Toast.LENGTH_SHORT).show()
                imgHeart.setImageResource(R.drawable.ic_heart)
                recyclerAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()
            }
        } else {
            val async = HomeRecyclerAdapter.DBAsyncTask(this, restaurantEntity, 3).execute()
            val result = async.get()
            if(result) {
                Toast.makeText(this,"Removed from favourites",Toast.LENGTH_SHORT).show()
                imgHeart.setImageResource(R.drawable.ic_heart_border)
                recyclerAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(this,"Some error occurred",Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}