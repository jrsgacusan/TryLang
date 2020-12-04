package com.example.trylang.buyer_activities

import android.os.Bundle
import android.view.View.OnTouchListener
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.trylang.R
import com.example.trylang.handlers.ServiceHandler
import com.example.trylang.handlers.UserHandler
import com.example.trylang.handlers.UserSellerInfoHandler
import com.example.trylang.handlers.UserSkillsHandler
import com.example.trylang.models.Service
import com.example.trylang.models.User
import com.example.trylang.models.UserSellerInfo
import com.example.trylang.models.UserSkills
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class DisplaySpecificServiceActivity : AppCompatActivity() {
    lateinit var toolBar: Toolbar

    lateinit var userUid: String
    lateinit var serviceUid: String
    //user -> this is for the first card view where all informations are from the model User
    lateinit var profileImageView: ImageView
    lateinit var nameTextView: TextView
    lateinit var numberTextView: TextView
    lateinit var bioTextView: TextView
    var userHandler = UserHandler()
    var userArrayList: ArrayList<User> = ArrayList()
    //user as seller info -> this is for the first card view where all informations are from the model UserSellerInfo
    lateinit var sellingDescriptionTextView: TextView
    lateinit var previousSchoolTextView: TextView
    lateinit var educationalAttainmentTextView: TextView
    var userSellerInfoHandler = UserSellerInfoHandler()
    var userSellerInfoArrayList: ArrayList<UserSellerInfo> = ArrayList()
    //List view for the skills
    lateinit var skillsListView : ListView
    var skillsArrayList: ArrayList<UserSkills> = ArrayList()
    var userSkillsHandler = UserSkillsHandler()
    lateinit var skillsArrayAdapter: ArrayAdapter<UserSkills>
    //for the service details
    lateinit var serviceTitleTextView: TextView
    lateinit var serviceDescriptionTextView: TextView
    lateinit var serviceCategoryTextView: TextView
    lateinit var priceTextView: TextView
    var serviceHandler = ServiceHandler()
    var serviceArrayList: ArrayList<Service> = ArrayList()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_display_specific_service)


        //intents
        userUid = intent.getStringExtra("userUid").toString()
        serviceUid = intent.getStringExtra("serviceUid").toString()


        //Tool bar
        toolBar = findViewById(R.id.toolBar_displaySpecificService)
        toolBar.setTitle("Specific Informations")
        toolBar.setNavigationOnClickListener {
            finish()
        }

        //Map user views for the user
        profileImageView = findViewById(R.id.profileImageView_acitivityDisplaySpecificActivity)
        nameTextView = findViewById(R.id.name_activityDisplaySpecificService)
        numberTextView = findViewById(R.id.number_acitivityDisplaySpecificService)
        bioTextView = findViewById(R.id.userBio_activityDisplaySpecificService)

        fetchUserData()

        //Map the views for the user seller info
        sellingDescriptionTextView = findViewById(R.id.sellingDescriptionTextView_activityDisplaySpecificService)
        previousSchoolTextView = findViewById(R.id.previousSchool_activityDisplaySpecificService)
        educationalAttainmentTextView = findViewById(R.id.educationalAttainment_activityDisplaySpecificService)

        fetchUserSellerInfoData()

        //Skills Array List
        skillsListView = findViewById(R.id.skillsListView_activityDisplaySpecificService)

        skillsListView.setOnTouchListener(OnTouchListener { v, event ->
            // Setting on Touch Listener for handling the touch inside ScrollView
            // Disallow the touch request for parent scroll on touch of child view
            v.parent.requestDisallowInterceptTouchEvent(true)
            false
        })

        fetchSkills()

        // For the last card view
        serviceTitleTextView = findViewById(R.id.serviceTitle_activityDisplaySpecificService)
        serviceDescriptionTextView = findViewById(R.id.serviceDescription_activityDisplaySpecificService)
        serviceCategoryTextView = findViewById(R.id.serviceCategory_dispalySpecificService )
        priceTextView = findViewById(R.id.price_activityDisplaySpecificService)

        fetchService()
    }

    private fun fetchService() {
        serviceHandler.serviceRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                serviceArrayList.clear()
                var serviceToBeDisplayed: Service? =null
                p0.children.forEach {
                    it -> val service = it.getValue(Service::class.java)
                    if (service!!.uid == serviceUid){
                        serviceToBeDisplayed = service
                    }
                }
                serviceTitleTextView.setText("${serviceToBeDisplayed!!.title}")
                serviceDescriptionTextView.setText("${serviceToBeDisplayed!!.description}")
                serviceCategoryTextView.setText("Category: ${serviceToBeDisplayed!!.category}")
                priceTextView.setText("₱${serviceToBeDisplayed!!.price}")
            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }

    private fun fetchSkills() {
        userSkillsHandler.userSkillsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                skillsArrayList.clear()
                p0.children.forEach {
                    val userSkills = it.getValue(UserSkills::class.java)
                    if (userSkills!!.userUid == userUid) {
                        skillsArrayList.add(userSkills!!)
                    }
                }
                skillsArrayAdapter = ArrayAdapter(applicationContext, android.R.layout.simple_list_item_1, skillsArrayList)
                skillsListView.adapter = skillsArrayAdapter

            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }


        })
    }

    private fun fetchUserSellerInfoData() {
        userSellerInfoHandler.userSellerInfoRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                userSellerInfoArrayList.clear()
                p0.children.forEach { it ->
                    val userSellerInfo = it.getValue(UserSellerInfo::class.java)
                    userSellerInfoArrayList.add(userSellerInfo!!)
                }
                val userData = userSellerInfoArrayList.find { it.uid == userUid }
                //Picasso is a caching manager for the image
                sellingDescriptionTextView.setText("${userData!!.description}")
                previousSchoolTextView.setText("${userData.previousSchool}")
                educationalAttainmentTextView.setText("${userData.educationalAttainment}")
            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })

    }

    private fun fetchUserData() {
        userHandler.userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                userArrayList.clear()
                p0.children.forEach { it ->
                    val user = it.getValue(User::class.java)
                    userArrayList.add(user!!)
                }
                val userData = userArrayList.find { it.uid == userUid }
                //Picasso is a caching manager for the image
                Picasso.get().load(userData!!.profileImageUrl).into(profileImageView)
                nameTextView.setText("${userData.firstName} ${userData.lastName}, ${userData.age}")
                numberTextView.setText("${userData.mobileNumber}")
                bioTextView.setText("${userData.bio}")
            }

            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }
}
