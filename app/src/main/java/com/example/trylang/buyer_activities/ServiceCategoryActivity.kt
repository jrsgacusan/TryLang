package com.example.trylang.buyer_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.example.trylang.R
import com.example.trylang.handlers.ServiceHandler
import com.example.trylang.handlers.UserHandler
import com.example.trylang.models.Service
import com.example.trylang.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder




class ServiceCategoryActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var servicesRecyclerView: RecyclerView
    var serviceHandler = ServiceHandler()
    var servicesArrayList: ArrayList<Service> = ArrayList()
    var currentUserUid = FirebaseAuth.getInstance().currentUser!!.uid
    lateinit var category: String
    var user: User? = null
    var userHandler = UserHandler()

    companion object{
        val USER_KEY = "USER_KEY"
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_service_category)
        //get the category we would display
        category = intent.getStringExtra("service_category").toString()
        //Map everything
        toolbar = findViewById(R.id.toolBar_activityServiceCategory)
        servicesRecyclerView = findViewById(R.id.serviceRecyclerView_activityServiceCategory)

        //Toolbar
        toolbar.setTitle(category)
        toolbar.setNavigationOnClickListener {
            finish()
        }


        val adapter = GroupAdapter<ViewHolder>()
        servicesRecyclerView.adapter = adapter

        fetchService()




    }

    private fun fetchService() {
        //register a listener to teverytime the database updates
        serviceHandler.serviceRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()
                servicesArrayList.clear()
                p0.children.forEach {
                    val service = it.getValue(Service::class.java)
                    if ((service!!.userUid != currentUserUid) && (service.category == category)){
                        adapter.add(ServiceItem(service))
                    }
                }
                adapter.setOnItemClickListener{ item, view ->

                    val serviceItem = item as ServiceItem
                    val intent = Intent(view.context, DisplaySpecificServiceActivity::class.java)
                    intent.putExtra("serviceUid", serviceItem.service.uid)
                    intent.putExtra("userUid", serviceItem.service.userUid)
                    Log.i("ServiceCategoryAgain","${serviceItem.service.userUid}" )
                    startActivity(intent)
                }
                servicesRecyclerView.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })

    }
}

class ServiceItem(val service: Service): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.findViewById<TextView>(R.id.title_serviceRow).text = service.title
        viewHolder.itemView.findViewById<TextView>(R.id.descriptionTextView_serviceRow).text = service.description
        viewHolder.itemView.findViewById<TextView>(R.id.priceTextView_serviceRow).text = "₱${service.price.toString()}"
        Picasso.get().load(service.userImageUrl).into(viewHolder.itemView.findViewById<ImageView>(R.id.imageView_serviceRow))
    }
    override fun getLayout(): Int {
        return R.layout.service_row
    }

}

