package com.example.trylang.sellerfragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trylang.R
import com.example.trylang.SellerActivity
import com.example.trylang.bottomNavigationSeller



class SellerServicesFragment : Fragment() {
    lateinit var itemToHide : MenuItem
    lateinit var v: View


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         v=  inflater.inflate(R.layout.fragment_seller_services, container, false)
        return v
    }

    companion object {

    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as SellerActivity?)?.setActionBarTitle("Services")
        bottomNavigationSeller.menu.findItem(R.id.Seller_servicesPage).setChecked(true)
    }
    //Whenever the fragment is not fragment_search, the action bar will appear
    override fun onStop() {
        super.onStop()


    }





}