package com.example.trylang.buyerfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trylang.*
import com.example.trylang.bottomNavigationBuyer


class HomeFragment : Fragment() {

    //search fragment = the SearchFragment class created
    val searchFragment = SearchFragment ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_home, container, false)
        //Map the button
        val btn = v.findViewById<Button>(R.id.searchBtn)
        //Button onclick listener
        btn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.wrapper, searchFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
                    }
        return v
    }
    //enable options menu in this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        (activity as BuyerActivity?)?.setActionBarTitle("Home")
        bottomNavigationBuyer.menu.findItem(R.id.homePage).setChecked(true)
    }









}