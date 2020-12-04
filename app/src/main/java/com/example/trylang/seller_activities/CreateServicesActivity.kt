package com.example.trylang.seller_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.example.trylang.R
import com.example.trylang.handlers.ServiceHandler
import com.example.trylang.handlers.UserHandler
import com.example.trylang.models.Service
import com.example.trylang.models.User
import com.example.trylang.sellerfragments.serviceGettingEdited
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso

class CreateServicesActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    lateinit var spinner: Spinner
    lateinit var titleEditText : EditText
    lateinit var descriptionEditText: EditText
    lateinit var priceEditText: EditText
    lateinit var button: Button
    var currentUserUid = FirebaseAuth.getInstance().uid
    var serviceHandler = ServiceHandler()
    lateinit var titleTextView: TextView
    lateinit var profileImageUrl : TextView
    var userHandler = UserHandler()
    var userArrayList: ArrayList<User> = ArrayList()
    var userData: User ? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_services)

        //Map the views from the layout
        profileImageUrl = findViewById(R.id.profileImageUrl_activityCreateServices)
        titleTextView = findViewById(R.id.titleTextView_createServices)
        spinner = findViewById(R.id.spinner_createServices)
        titleEditText = findViewById(R.id.title_createServices)
        descriptionEditText = findViewById(R.id.description_createServices)
        priceEditText = findViewById(R.id.price_createServices)
        button = findViewById(R.id.button_createServices)
        //Add the different service categories to the spinner
        ArrayAdapter.createFromResource(this, R.array.services_category, android.R.layout.simple_spinner_item)
            .also {
                    adapter -> adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        spinner.onItemSelectedListener = this
        //Check if the value of Service getting edited is null or not
        checkFirst()
        //Button listener
        button.setOnClickListener{
            if (button.text.toString() == "Create" ){
                checkAndCreate()
            }
            if (button.text.toString() == "Update"){
                update()
                serviceGettingEdited = null
                finish()
            }


        }
    }

    override fun onStart() {
        super.onStart()
        userHandler.userRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                userArrayList.clear()
                p0.children.forEach {
                        it -> val user = it.getValue(User::class.java)
                    userArrayList.add(user!!)
                }
                userData = userArrayList.find { it.uid == currentUserUid }
                profileImageUrl.setText(userData!!.profileImageUrl)
            }
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }

    fun checkFirst(){
        if (serviceGettingEdited == null) {
            titleTextView.setText("FILL UP THE FOLLOWING")
            button.setText("Create")
        } else if (serviceGettingEdited!= null) {
            button.setText("Update")
            titleTextView.setText("UPDATE")
            //Set the values of the views using the global variable serviceGettingEdited
            titleEditText.setText(serviceGettingEdited!!.title)
            descriptionEditText.setText(serviceGettingEdited!!.description)
            priceEditText.setText(serviceGettingEdited!!.price.toString())
        }

    }
    fun update(){
        val service = Service(userImageUrl = profileImageUrl.text.toString(), uid = serviceGettingEdited!!.uid, title = titleEditText.text.toString(), description = descriptionEditText.text.toString() ,price = priceEditText.text.toString().toInt() , category = spinner.selectedItem.toString(), userUid = currentUserUid)
        if(serviceHandler.updateService(service)){
            Toast.makeText(this, "Service updated", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAndCreate() {
        if(titleEditText.text.toString().isEmpty()){
            titleEditText.error = "Please fill this up"
            titleEditText.requestFocus()
            return
        }

        if(descriptionEditText.text.toString().isEmpty()){
            descriptionEditText.error = "Please fill this up"
            descriptionEditText.requestFocus()
            return
        }
        if(priceEditText.text.toString().isEmpty()){
            priceEditText.error = "Please fill this up"
            priceEditText.requestFocus()
            return
        }

        val service = Service( userImageUrl = profileImageUrl.text.toString(), title = titleEditText.text.toString(), description = descriptionEditText.text.toString() ,price = priceEditText.text.toString().toInt() , category = spinner.selectedItem.toString(), userUid = currentUserUid)
        if(serviceHandler.createService(service)){
            Toast.makeText(this, "Service Added", Toast.LENGTH_SHORT).show()
        }

        finish()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }


}