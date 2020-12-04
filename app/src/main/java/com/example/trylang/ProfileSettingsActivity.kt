package com.example.trylang

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.core.view.isVisible
import com.example.trylang.buyer_activities.BuyerActivity
import com.example.trylang.handlers.UserHandler
import com.example.trylang.models.User
import com.example.trylang.seller_activities.SellerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class ProfileSettingsActivity : AppCompatActivity() {
    lateinit var userHandler: UserHandler
    var userData : User? = null
    lateinit var userArrayList: ArrayList<User>
    lateinit var uid: String
    lateinit var firstNameProfileEditText: EditText
    lateinit var lastNameProfileEditText: EditText
    lateinit var emailAddressProfileEditText: EditText
    lateinit var mobileNumberEditText: EditText
    lateinit var ageEditText:EditText
    lateinit var bioEditText: EditText
    lateinit var profileImageView: ImageView
    lateinit var button: Button
    lateinit var uploadNewImage: ImageView
    lateinit var userUid : TextView
    lateinit var imageUrl: TextView
    private val pickImage = 100
    var tryThis: Uri? = null





    lateinit var profileSettingsToolbar: androidx.appcompat.widget.Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile_settings)
        val lastActivity = intent.getStringExtra("intent")

        userArrayList = ArrayList()
        //Map Everything
        imageUrl = findViewById(R.id.profileImageUrl_profileSettings)
        userUid = findViewById(R.id.userUid_profileSettings)
        uploadNewImage= findViewById(R.id.uploadNewImage_profileSettings)
        button = findViewById(R.id.btn_profileSettings)
        profileImageView = findViewById(R.id.profileImageView_profileSettings)
        firstNameProfileEditText = findViewById(R.id.firstNameEditText_profileSettings)
        profileSettingsToolbar = findViewById(R.id.profileSettingToolBar)
        lastNameProfileEditText = findViewById(R.id.lastNameEditText_profileSettings)
        emailAddressProfileEditText = findViewById(R.id.emailAddEditText_profileSettings)
        mobileNumberEditText = findViewById(R.id.mobileNumberEditText_profileSettings)
        ageEditText = findViewById(R.id.ageEditText_profileSettings)
        bioEditText = findViewById(R.id.userBio_profileSettings)
        // Initialize Database Handler
        userHandler = UserHandler()
        //Get the current UID of the logged in user
        uid = FirebaseAuth.getInstance().uid!!
        //Set toolbar navigation Icon
        profileSettingsToolbar.setNavigationIcon(R.drawable.ic_back)
        profileSettingsToolbar.setNavigationOnClickListener{
            goBack(lastActivity)
        }
        //Read user data
        readUserData()

        //Button listener
        button.setOnClickListener{
            if (button.text.toString()== "Update"){
                executeUpdate()
            } else if (button.text.toString()== "Save" ){
                executeSave()
            }

        }
        uploadNewImage.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, pickImage)
        }

    }

    fun executeUpdate(){
        //enable the views that will be edited
        firstNameProfileEditText.isEnabled = true
        lastNameProfileEditText.isEnabled = true
        mobileNumberEditText.isEnabled = true
        ageEditText.isEnabled = true
        bioEditText.isEnabled = true
        uploadNewImage.isVisible = true
        //Change the text of the button
        button.setText("Save")
    }
    fun executeSave(){
        uploadNewImage.isVisible = false
        firstNameProfileEditText.isEnabled = false
        lastNameProfileEditText.isEnabled = false
        mobileNumberEditText.isEnabled = false
        ageEditText.isEnabled = false
        bioEditText.isEnabled = false
        //Check if there is an image selected
        if (selectedPhotoUri != null){
            uploadImageToFirebaseDatabase()
        }
        else {
            saveUserToFireBaseDatabase(imageUrl.text.toString())
        }
        //Sets the selected photo to null again
        selectedPhotoUri = null

        button.setText("Update")
    }
    //Uploading an image to image view override function
    var selectedPhotoUri : Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            selectedPhotoUri = data!!.data
            profileImageView.setImageURI(selectedPhotoUri)
        }
    }
    private fun uploadImageToFirebaseDatabase() {
        if (selectedPhotoUri != null) {
            val fileName = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToFireBaseDatabase(it.toString())
                    }
                }
        }
    }
    private fun saveUserToFireBaseDatabase(profileImageUrl: String) {
        val user = User(
            uid = userUid.text.toString(),
            bio = bioEditText.text.toString() ,
            emailAddress =  emailAddressProfileEditText.text.toString(),
            profileImageUrl = profileImageUrl,
            firstName =  firstNameProfileEditText.text.toString(),
            lastName = lastNameProfileEditText.text.toString(),
            age = ageEditText.text.toString().toInt(),
            mobileNumber = mobileNumberEditText.text.toString())
        if(userHandler.update(user)){
            Toast.makeText(this, "Account info updated", Toast.LENGTH_SHORT).show()
        }
    }
    private fun goBack(lastActivity: String?) {
        if (lastActivity == "seller") {
            startActivity(Intent(this, SellerActivity::class.java))
        }
        if (lastActivity == "buyer") {
            startActivity(Intent(this, BuyerActivity::class.java))
        }
        finish()
    }
    fun readUserData(){
        userHandler.userRef.addValueEventListener(object: ValueEventListener{
            override fun onDataChange(p0: DataSnapshot) {
                userArrayList.clear()
                p0.children.forEach {
                    it -> val user = it.getValue(User::class.java)
                    userArrayList.add(user!!)
                }
                Log.i ("ProfileSettings",  "ArrayListContents:${userArrayList}")
                userData = userArrayList.find { it.uid == uid }
                firstNameProfileEditText.setText(userData!!.firstName)
                lastNameProfileEditText.setText(userData!!.lastName)
                emailAddressProfileEditText.setText(userData!!.emailAddress)
                mobileNumberEditText.setText(userData!!.mobileNumber)
                ageEditText.setText(userData!!.age.toString())
                bioEditText.setText(userData!!.bio)
                userUid.setText(userData!!.uid)
                imageUrl.setText(userData!!.profileImageUrl)


                //Picasso is a caching manager for the image
                Picasso.get().load(userData!!.profileImageUrl).into(profileImageView)
            }
            override fun onCancelled(p0: DatabaseError) {
                //TODO("Not yet implemented")
            }
        })
    }



}