package com.harshal.internshalatrainingsproject.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.harshal.internshalatrainingsproject.R
import com.harshal.internshalatrainingsproject.adapter.HomeRecyclerAdapter
import com.harshal.internshalatrainingsproject.model.Restaurant

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

    val restaurantList  = arrayListOf<Restaurant>(
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, true),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false),
        Restaurant("Name", "Price", "4.0", R.drawable.ic_launcher_foreground, false)
    )
    lateinit var recyclerAdapter: HomeRecyclerAdapter

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
        recyclerAdapter = HomeRecyclerAdapter(activity as Context, restaurantList)

        recyclerHome.adapter = recyclerAdapter
        recyclerHome.layoutManager = layoutManager
        recyclerHome.addItemDecoration(DividerItemDecoration(recyclerHome.context, (layoutManager as LinearLayoutManager).orientation))


        return view
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