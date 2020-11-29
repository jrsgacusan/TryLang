package com.example.trylang.buyerfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.trylang.BuyerActivity
import com.example.trylang.R
import com.example.trylang.SellerActivity
import com.example.trylang.bottomNavigationBuyer
import com.google.android.material.snackbar.Snackbar
import android.content.Intent as Intent1


class ProfileFragment : Fragment() {

    lateinit var switch: Switch
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_profile, container, false)
        //map the views of the layout file
        switch = v.findViewById<Switch>(R.id.seller_modeSwitch)
        //Apply listeners
        switch.setOnClickListener {

            changeMode (switch.isChecked)
            Toast.makeText(this.context, "Switched to Seller Mode", Toast.LENGTH_LONG).show()

        } //Switch view
        return v
    }
    //Change the mode based from the switch view
    private fun changeMode (state : Boolean){
        if (state == true) {
            switch.isChecked = false
            val intent = Intent1(this.context, SellerActivity::class.java )
            //Add a snack bar here to notify that the mode has changed
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as BuyerActivity?)?.setActionBarTitle("Profile Page")
        bottomNavigationBuyer.menu.findItem(R.id.profilePage).setChecked(true)
    }

    companion object {

    }
}