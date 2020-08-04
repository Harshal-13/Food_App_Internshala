package com.harshal.internshalatrainingsproject.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.adapter.HomeRecyclerAdapter
import com.harshal.internshalatrainingsproject.model.Restaurant
import com.harshal.internshalatrainingsproject.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//private const val ARG_PARAM1 = "param1"
//private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null

    lateinit var recyclerHome : RecyclerView
    lateinit var layoutManager: LinearLayoutManager
    var restaurantList = arrayListOf<Restaurant>()
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressLayout : RelativeLayout
    lateinit var progressBar: ProgressBar

    var ratingComparator = Comparator<Restaurant> { res1, res2 ->
        if(res1.restaurantRating.compareTo(res2.restaurantRating, true)==0){
            res1.restaurantName.compareTo(res2.restaurantName, true)
        } else {
            res1.restaurantRating.compareTo(res2.restaurantRating, true)
        }
    }
    var priceComparator = Comparator<Restaurant> { res1, res2 ->
        if(res1.restaurantPrice.compareTo(res2.restaurantPrice, true)==0){
            res1.restaurantName.compareTo(res2.restaurantName, true)
        } else {
            res1.restaurantPrice.compareTo(res2.restaurantPrice, true)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)
        progressLayout = view.findViewById(R.id.progressLayout)
        progressBar = view.findViewById(R.id.progressBar)

        progressLayout.visibility = View.VISIBLE

        setHasOptionsMenu(true)

        val queue = Volley.newRequestQueue(activity as Context)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/"

        if(ConnectionManager().checkConnectivity(activity as Context)){

            val jsonObjectRequest = object : JsonObjectRequest(Request.Method.GET, url, null, Response.Listener {

                try {
                    progressLayout.visibility = View.GONE

                    val res = it.getJSONObject("data")
                    val success = res.getBoolean("success")
                    if(success){
                        val data = res.getJSONArray("data")
                        for( i in 0 until data.length()){
                            val restaurantJsonObject = data.getJSONObject(i)
                            val restaurantObject = Restaurant(
                                restaurantJsonObject.getString("id"),
                                restaurantJsonObject.getString("name"),
                                restaurantJsonObject.getString("rating"),
                                "₹"+restaurantJsonObject.getString("cost_for_one")+"/person",
                                restaurantJsonObject.getString("image_url")
                            )
                            restaurantList.add(restaurantObject)
                        }
                        recyclerAdapter = HomeRecyclerAdapter(activity as Context, restaurantList)

                        recyclerHome.adapter = recyclerAdapter
                        recyclerHome.layoutManager = layoutManager
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {

        inflater.inflate(R.menu.menu_home_sort, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.itemRatingL){
            Collections.sort(restaurantList,ratingComparator)
        } else if(id == R.id.itemRatingH){
            Collections.sort(restaurantList,ratingComparator)
            restaurantList.reverse()
        } else if(id == R.id.itemPriceL){
            Collections.sort(restaurantList, priceComparator)
        } else if(id == R.id.itemPriceH){
            Collections.sort(restaurantList, priceComparator)
            restaurantList.reverse()
        }
        recyclerAdapter.notifyDataSetChanged()
        return super.onOptionsItemSelected(item)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
            }
    }
}