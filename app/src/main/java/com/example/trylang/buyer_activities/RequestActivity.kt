package com.example.trylang.buyer_activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.trylang.R
import com.example.trylang.handlers.ServiceRequestHandler
import com.example.trylang.models.ServiceRequest
import com.google.firebase.auth.FirebaseAuth

class RequestActivity : AppCompatActivity() {
    lateinit var titleTextView: TextView
    lateinit var titleEditText: EditText
    lateinit var descriptionEditText: EditText
    lateinit var priceEditText: EditText
    lateinit var button: Button

    var serviceRequestHandler = ServiceRequestHandler()
    var currentUserUid = FirebaseAuth.getInstance().uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request)

        //Map everything
        titleTextView = findViewById(R.id.titleTextView_activityRequest)
        titleEditText = findViewById(R.id.title_activityRequest)
        descriptionEditText = findViewById(R.id.description_activityRequest)
        priceEditText = findViewById(R.id.price_activityRequest)
        button = findViewById(R.id.button_requestActivity)


        checkFirst()
        button.setOnClickListener{
            if(button.text.toString() == "Create"){
                checkAndCreate()
                finish()
            } else if (button.text.toString() == "Update"){
                update()
                serviceRequestGettingEdited = null
                finish()
            }
        }


    }

    fun checkAndCreate(){
        if (titleEditText.text.toString().isEmpty()){
            titleEditText.error = "Fill up the title"
            titleEditText.requestFocus()
            return
        }

        if (descriptionEditText.text.toString().isEmpty()){
            descriptionEditText.error = "Add a description"
            descriptionEditText.requestFocus()
            return
        }

        if (priceEditText.text.toString().isEmpty()){
            priceEditText.error = "What is your target price."
            priceEditText.requestFocus()
            return
        }

        val serviceRequest = ServiceRequest(
            userUid = currentUserUid,
            description =  descriptionEditText.text.toString(),
            title = titleEditText.text.toString(),
            price = priceEditText.text.toString().toInt()
        )
        if(serviceRequestHandler.createServiceRequest(serviceRequest)){
            Toast.makeText(this, "Request posted.", Toast.LENGTH_SHORT).show()
        }

    }

    fun checkFirst(){
        if (serviceRequestGettingEdited == null) {
            titleTextView.setText("CREATE REQUEST")
            button.setText("Create")
        } else if (serviceRequestGettingEdited != null) {
            button.setText("Update")
            titleTextView.setText("UPDATE")
            //Set the values of the views using the global variable serviceGettingEdited
            titleEditText.setText(serviceRequestGettingEdited!!.title)
            descriptionEditText.setText(serviceRequestGettingEdited!!.description)
            priceEditText.setText(serviceRequestGettingEdited!!.price.toString())
        }

    }

    fun update(){
        val serviceRequest = ServiceRequest(uid = serviceRequestGettingEdited!!.uid, title = titleEditText.text.toString(), description = descriptionEditText.text.toString() ,price = priceEditText.text.toString().toInt(), userUid = currentUserUid)
        if(serviceRequestHandler.updateServiceRequest(serviceRequest)){
            Toast.makeText(this, "Service request updated", Toast.LENGTH_SHORT).show()
        }
    }


}