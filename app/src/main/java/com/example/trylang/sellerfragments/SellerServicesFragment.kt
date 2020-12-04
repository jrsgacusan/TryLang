package com.example.trylang.sellerfragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.trylang.seller_activities.CreateServicesActivity
import com.example.trylang.R
import com.example.trylang.seller_activities.SellerActivity
import com.example.trylang.seller_activities.bottomNavigationSeller
import com.example.trylang.handlers.ServiceHandler
import com.example.trylang.models.Service
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

var serviceGettingEdited: Service? = null
class SellerServicesFragment : Fragment() {
    lateinit var v: View
    lateinit var serviceslistView: ListView
    lateinit var servicesArrayList: ArrayList<Service>
    lateinit var servicesArrayAdapter: ArrayAdapter<Service>
    var currentUserUid = FirebaseAuth.getInstance().uid
    var serviceHandler = ServiceHandler()




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onStart() {
        super.onStart()
        //register a listener to teverytime the database updates
        serviceHandler.serviceRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                servicesArrayList.clear()
                p0.children.forEach {
                        it -> val service = it.getValue(Service::class.java)
                    if (service!!.userUid == currentUserUid){
                    servicesArrayList.add(service!!)
                    }

                }

                servicesArrayAdapter = ArrayAdapter(v.context, android.R.layout.simple_list_item_1, servicesArrayList)
                serviceslistView.adapter = servicesArrayAdapter



            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
         v =  inflater.inflate(R.layout.fragment_seller_services, container, false)
        servicesArrayList = ArrayList()
        serviceslistView = v.findViewById(R.id.listView_sellerServicesFragment)
        registerForContextMenu(serviceslistView)
        return v
    }


    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        getActivity()?.getMenuInflater()?.inflate(R.menu.menu_edit_delete, menu);
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val info = item.menuInfo as AdapterView.AdapterContextMenuInfo
        return when (item.itemId) {
            R.id.edit -> {
                serviceGettingEdited = servicesArrayList[info.position]
                startActivity(Intent(v.context, CreateServicesActivity::class.java))
                true
            }
            R.id.delete -> {
                if (serviceHandler.deleteService(servicesArrayList[info.position])) {
                    Toast.makeText(v.context, "Service deleted", Toast.LENGTH_SHORT).show()
                }
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    companion object {

    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as SellerActivity?)?.setActionBarTitle("Services")
        bottomNavigationSeller.menu.findItem(R.id.Seller_servicesPage).setChecked(true)
        (activity as SellerActivity).menuToHide.setVisible(true)
        serviceGettingEdited = null
    }
    override fun onStop() {
        super.onStop()
        (activity as SellerActivity).menuToHide.setVisible(false)


    }





}