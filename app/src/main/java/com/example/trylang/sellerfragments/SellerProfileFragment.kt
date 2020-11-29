package com.example.trylang.sellerfragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Switch
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.trylang.BuyerActivity
import com.example.trylang.R
import com.example.trylang.SellerActivity
import com.example.trylang.bottomNavigationSeller


class SellerProfileFragment : Fragment() {

    lateinit var switchBtn : Switch
    val sellerServices = SellerServicesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_seller_profile, container, false)

        //map the views of the layout file
        switchBtn = v.findViewById<Switch>(R.id.seller_modeSwitch)
        val services = v.findViewById<CardView>(R.id.seller_myServicesCardView)
        //Add listeners
        //Switch button view
        switchBtn.setOnClickListener {
            changeMode(switchBtn.isChecked)
            Toast.makeText(this.context, "Switched to Buyer Mode", Toast.LENGTH_LONG).show()
        }
        //Services card view
        services.setOnClickListener{
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.wrapper, sellerServices)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
        }



        return v
    }



    //Change the mode based from the switch view
    private fun changeMode(state: Boolean){
        if (state == false) {
            switchBtn.isChecked = true
            val intent = Intent(this.context, BuyerActivity::class.java)
            startActivity(intent)

        }
    }

    companion object {

    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as SellerActivity?)?.setActionBarTitle("Profile Page")
        bottomNavigationSeller.menu.findItem(R.id.Seller_profilePage).setChecked(true)
    }






}