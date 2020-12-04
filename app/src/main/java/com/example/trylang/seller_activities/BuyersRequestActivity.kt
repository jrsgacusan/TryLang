package com.example.trylang.seller_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.widget.Toolbar
import com.example.trylang.R
import com.example.trylang.handlers.ServiceRequestHandler
import com.example.trylang.models.ServiceRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class BuyersRequestActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var serviceRequestlistView: ListView
    lateinit var serviceRequestArrayList: ArrayList<ServiceRequest>
    lateinit var serviceRequestArrayAdapter: ArrayAdapter<ServiceRequest>
    var currentUserUid = FirebaseAuth.getInstance().uid
    var serviceRequestHandler = ServiceRequestHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyers_request)

        toolbar = findViewById(R.id.toolBar_activityBuyersRequest)
        serviceRequestlistView = findViewById(R.id.buyersRequestListView_activityBuyersRequest)
        serviceRequestArrayList = ArrayList()

        toolbar.setTitle("Buyer Requests")
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }






    }
    override fun onStart() {
        super.onStart()
        //register a listener to teverytime the database updates
        serviceRequestHandler.serviceRequestRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                serviceRequestArrayList.clear()
                p0.children.forEach {
                    it -> val serviceRequest = it.getValue(ServiceRequest::class.java)
                    if (serviceRequest!!.userUid != currentUserUid){
                        serviceRequestArrayList.add(serviceRequest!!)
                    }

                }
                serviceRequestArrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, serviceRequestArrayList)
                serviceRequestlistView.adapter = serviceRequestArrayAdapter
            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }



}