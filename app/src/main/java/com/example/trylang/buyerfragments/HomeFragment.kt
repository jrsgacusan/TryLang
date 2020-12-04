package com.example.trylang.buyerfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.trylang.*
import com.example.trylang.buyer_activities.BuyerActivity
import com.example.trylang.buyer_activities.ServiceCategoryActivity
import com.example.trylang.buyer_activities.bottomNavigationBuyer


class HomeFragment : Fragment() {

    lateinit var homeRepairCardView: CardView
    lateinit var homeCleaningCardView: CardView
    lateinit var airconCardView: CardView
    lateinit var plumbingCardView: CardView
    lateinit var electricalCardView: CardView
    lateinit var computerRepairCardView: CardView
    lateinit var movingCardView: CardView
    lateinit var deliveryCardView: CardView
    var intentString: String? = null


    lateinit var v: View
    val searchFragment = SearchFragment ()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_home, container, false)
        //Map everything here
        homeRepairCardView = v.findViewById(R.id.homeRepairCardView)
        homeCleaningCardView = v.findViewById(R.id.homeCleaningCardView)
        airconCardView = v.findViewById(R.id.airconCardView)
        plumbingCardView = v.findViewById(R.id.plumbingCardView)
        electricalCardView = v.findViewById(R.id.electricalCardView)
        computerRepairCardView = v.findViewById(R.id.computerRepairCardView)
        movingCardView = v.findViewById(R.id.movingCardView)
        deliveryCardView = v.findViewById(R.id.deliveryCardView)
        val btn = v.findViewById<Button>(R.id.searchBtn)
        //Button onclick listener
        btn.setOnClickListener {
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            transaction?.replace(R.id.wrapper, searchFragment)
            transaction?.disallowAddToBackStack()
            transaction?.commit()
                    }
        //Listener for the different services
        homeRepairCardView.setOnClickListener {
            intentString = "Home Repair"
            goToServicesCategory()
        }
        homeCleaningCardView.setOnClickListener {
            intentString = "Home Cleaning"
            goToServicesCategory()
        }
        airconCardView.setOnClickListener {
            intentString = "Aircon"
            goToServicesCategory()
        }
        plumbingCardView.setOnClickListener {
            intentString = "Plumbing"
            goToServicesCategory()
        }
        electricalCardView.setOnClickListener {
            intentString = "Electrical, Wiring, Lighting"
            goToServicesCategory()
        }
        computerRepairCardView.setOnClickListener {
            intentString = "Computer Repair"
            goToServicesCategory()
        }
        movingCardView.setOnClickListener {
            intentString = "Moving"
            goToServicesCategory()
        }
        deliveryCardView.setOnClickListener {
            intentString = "Delivery"
            goToServicesCategory()
        }


        return v
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onResume() {
        super.onResume()
        (activity as BuyerActivity?)?.setActionBarTitle("Home")
        bottomNavigationBuyer.menu.findItem(R.id.homePage).setChecked(true)
    }

    fun goToServicesCategory(){
        val intent= Intent(v.context, ServiceCategoryActivity::class.java )
        intent.putExtra("service_category", intentString)
        startActivity(intent)
        intentString = null

    }









}