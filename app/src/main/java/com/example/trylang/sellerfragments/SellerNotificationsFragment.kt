package com.example.trylang.sellerfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.trylang.R
import com.example.trylang.seller_activities.SellerActivity
import com.example.trylang.seller_activities.bottomNavigationSeller


class SellerNotificationsFragment : Fragment() {
    // TODO: Rename and change types of parameters


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seller_notifications, container, false)
    }

    companion object {

    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as SellerActivity?)?.setActionBarTitle("Notifications")
        bottomNavigationSeller.menu.findItem(R.id.Seller_notificationsPage).setChecked(true)
    }
}