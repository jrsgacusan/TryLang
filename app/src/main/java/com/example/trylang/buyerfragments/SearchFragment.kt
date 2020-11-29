package com.example.trylang.buyerfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trylang.BuyerActivity
import com.example.trylang.R
import com.example.trylang.bottomNavigationBuyer


class SearchFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v=  inflater.inflate(R.layout.fragment_search, container, false)
        //Map the Search View
        val searchView = v.findViewById<SearchView>(R.id.searchView)
        searchView.setIconified(false) //automatically opens the search view. As soon as this fragment is used, the search view is already clicked.
        return v
    }
    //Whenever the fragment is changed to fragment_search, the action bar will disappear
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
        bottomNavigationBuyer.menu.findItem(R.id.search).setChecked(true)
    }
    //Whenever the fragment is not fragment_search, the action bar will appear
    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

    }



}