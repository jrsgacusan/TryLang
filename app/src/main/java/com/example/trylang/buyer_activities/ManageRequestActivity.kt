package com.example.trylang.buyer_activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.Toolbar
import com.example.trylang.R
import com.example.trylang.handlers.ServiceRequestHandler
import com.example.trylang.models.ServiceRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener


var serviceRequestGettingEdited: ServiceRequest? = null
class ManageRequestActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var serviceRequestlistView: ListView
    lateinit var serviceRequestArrayList: ArrayList<ServiceRequest>
    lateinit var serviceRequestArrayAdapter: ArrayAdapter<ServiceRequest>
    var currentUserUid = FirebaseAuth.getInstance().uid
    var serviceRequestHandler = ServiceRequestHandler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_request)
        toolbar = findViewById(R.id.toolBar_activityManageRequest)
        serviceRequestlistView = findViewById(R.id.requestListView_activityManageRequest)
        serviceRequestArrayList = ArrayList()
        toolbar.setTitle("Manage Requests")
        toolbar.setNavigationIcon(R.drawable.ic_back)
        toolbar.setNavigationOnClickListener{
            finish()
        }
        registerForContextMenu(serviceRequestlistView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_edit_delete, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.edit -> {
                serviceRequestGettingEdited = serviceRequestArrayList[info.position]
                startActivity(Intent(this, RequestActivity::class.java))
                true
            }
            R.id.delete -> {
                if (serviceRequestHandler.deleteServiceRequest(serviceRequestArrayList[info.position])) {
                    Toast.makeText(this, "Service request deleted", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
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
                    if (serviceRequest!!.userUid == currentUserUid){
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