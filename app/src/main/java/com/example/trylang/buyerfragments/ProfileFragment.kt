package com.example.trylang.buyerfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import com.example.trylang.*
import com.example.trylang.buyer_activities.BuyerActivity
import com.example.trylang.buyer_activities.ManageRequestActivity
import com.example.trylang.buyer_activities.RequestActivity
import com.example.trylang.buyer_activities.bottomNavigationBuyer
import com.example.trylang.handlers.UserHandler
import com.example.trylang.models.User
import com.example.trylang.seller_activities.SellerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import android.content.Intent as Intent1


class ProfileFragment : Fragment() {

    lateinit var switch: Switch
    //Used to fetch data
    var userHandler = UserHandler()
    var userArrayList: ArrayList<User> = ArrayList()
    var userData : User? = null
    var uid = FirebaseAuth.getInstance().uid
    //until here
    lateinit var v: View
    lateinit var postRequestCardView: CardView
    lateinit var manageRequestCardView: CardView

    var auth = FirebaseAuth.getInstance().currentUser


    lateinit var profileImage : ImageView
    lateinit var name: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchUserData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_profile, container, false)
        //map the views of the layout file
        switch = v.findViewById<Switch>(R.id.seller_modeSwitch)
        profileImage = v.findViewById(R.id.profileImage_fragmentBuyerProfile)
        name = v.findViewById(R.id.userName_fragmentBuyerProfile)
        postRequestCardView = v.findViewById(R.id.postARequest_cardView_fragmentProfile)
        manageRequestCardView = v.findViewById(R.id.manageRequestCardView_fragmentProfile)



        //Apply listeners
        switch.setOnClickListener {

            changeMode (switch.isChecked)
            Toast.makeText(this.context, "Switched to Seller Mode", Toast.LENGTH_LONG).show()

        }
        //Post Request
        postRequestCardView.setOnClickListener{
            val intent = Intent1(v.context, RequestActivity::class.java)
            startActivity(intent)
        }
        //Manage Request
        manageRequestCardView.setOnClickListener{
            val intent = Intent1(v.context, ManageRequestActivity::class.java)
            startActivity(intent)

        }

        return v
    }
    //Change the mode based from the switch view
    private fun changeMode (state : Boolean){
        if (state == true) {
            switch.isChecked = false
            val intent = Intent1(this.context, SellerActivity::class.java )
            intent.flags = android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK.or(android.content.Intent.FLAG_ACTIVITY_NEW_TASK)
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

    private fun fetchUserData() {
        userHandler.userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                userArrayList.clear()
                p0.children.forEach {
                        it -> val user = it.getValue(User::class.java)
                    userArrayList.add(user!!)
                }
                userData = userArrayList.find { it.uid == uid }
                //Picasso is a caching manager for the image
               if(auth != null){
                   name.setText("${userData!!.firstName} ${userData!!.lastName}")
                   Picasso.get().load(userData!!.profileImageUrl).into(profileImage)
               }


            }
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }
}