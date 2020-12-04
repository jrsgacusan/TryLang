package com.example.trylang.sellerfragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.example.trylang.buyer_activities.BuyerActivity
import com.example.trylang.R
import com.example.trylang.seller_activities.SellerActivity
import com.example.trylang.seller_activities.bottomNavigationSeller
import com.example.trylang.handlers.UserHandler
import com.example.trylang.models.User
import com.example.trylang.seller_activities.AboutMeAsSellerActivity
import com.example.trylang.seller_activities.BuyersRequestActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class SellerProfileFragment : Fragment() {

    lateinit var switchBtn : Switch
    val sellerServices = SellerServicesFragment()
    lateinit var profileImage: ImageView
    lateinit var nameTextView : TextView
    lateinit var services: CardView
    lateinit var buyersRequestCardView: CardView
    lateinit var aboutMeAsASellerCardView: CardView
    lateinit var v: View
    var userHandler = UserHandler()
    var userArrayList: ArrayList<User> = ArrayList()
    var userData : User? = null
    var uid = FirebaseAuth.getInstance().uid

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_seller_profile, container, false)

        //map the views of the layout file
        aboutMeAsASellerCardView = v.findViewById(R.id.seller_aboutMeCardView)
        switchBtn = v.findViewById<Switch>(R.id.seller_modeSwitch)
        services = v.findViewById<CardView>(R.id.seller_myServicesCardView)
        profileImage = v.findViewById(R.id.profileImage_fragmentSellerProfile)
        nameTextView = v.findViewById(R.id.userName_fragmentSellerProfile)
        buyersRequestCardView = v.findViewById(R.id.buyersRequestCardView_fragmentSellerProfile)

        //Fetch user data
        fetchUserData()

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
        //Buyers Request
        buyersRequestCardView.setOnClickListener {
            val intent = Intent(v.context, BuyersRequestActivity::class.java)
            startActivity(intent)
        }
        //About me as a seller
        aboutMeAsASellerCardView.setOnClickListener {
            val intent = Intent(v.context, AboutMeAsSellerActivity::class.java)
            startActivity(intent)

        }



        return v
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
                nameTextView.setText("${userData!!.firstName} ${userData!!.lastName}")
                Picasso.get().load(userData!!.profileImageUrl).into(profileImage)
            }
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }


    //Change the mode based from the switch view
    private fun changeMode(state: Boolean){
        if (state == false) {
            switchBtn.isChecked = true
            val intent = Intent(this.context, BuyerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)

        }
    }

    companion object {

    }

    override fun onResume() {
        super.onResume()
        // Set title bar
        (activity as SellerActivity?)?.setActionBarTitle("Profile Page")
        (activity as SellerActivity).menuItem =
        bottomNavigationSeller.menu.findItem(R.id.Seller_profilePage).setChecked(true)

    }






}