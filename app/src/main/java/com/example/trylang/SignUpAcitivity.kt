package com.example.trylang

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.trylang.handlers.UserHandler
import com.example.trylang.handlers.UserSkillsHandler
import com.example.trylang.models.User
import com.example.trylang.models.UserSellerInfo
import com.example.trylang.models.UserSkills
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.io.InputStream
import java.util.*

class SignUpAcitivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    lateinit var profileImageView: ImageView
    lateinit var uploadImageView: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null
    lateinit var fname: EditText
    lateinit var lname: EditText
    lateinit var email: EditText
    lateinit var mobileNum: EditText
    lateinit var password: EditText
    lateinit var retypePass: EditText
    lateinit var age: TextView
    private lateinit var auth: FirebaseAuth
    lateinit var userHandler: UserHandler




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        userHandler = UserHandler()
        auth = FirebaseAuth.getInstance()

        //Map the layout file views to the Kotlin file
        fname = findViewById<EditText>(R.id.firstNameEditText)
        lname = findViewById<EditText>(R.id.lastNameEditText)
        email = findViewById<EditText>(R.id.emailAddEditText)
        mobileNum = findViewById<EditText>(R.id.mobileNumberEditText)
        password = findViewById<EditText>(R.id.passWordEditText)
        retypePass = findViewById<EditText>(R.id.retypePasswordEditText)
        age = findViewById<TextView>(R.id.ageTextView)
        val seekBar : SeekBar = findViewById(R.id.ageSeekBar) as SeekBar
        val signUpBtn = findViewById<Button>(R.id.signUpBtn)
        profileImageView = findViewById(R.id.profileImageView) //Image Views within the layout file
        uploadImageView = findViewById<ImageView>(R.id.uploadImageView) //Image Views within the layout file

        //popup menu for the upload image view listener
        uploadImageView.setOnClickListener {showMenu(uploadImageView)}

        //seek bar
        //age seek bar 18-60
        val step = 1 // 1 step per scroll
        val max = 60 //Maximum age
        val min = 18 //Minimum age
        seekBar.setMax((max - min) / step)
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, i: Int, fromUser: Boolean) {
                age.text = (min + (i * step)).toString()

            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })

        //signUpBtn onclick listener
        signUpBtn.setOnClickListener{
            authenticate(it)
        }


    }

    //Popup Menu for image view
    fun showPopup(v: View) {
        val popup = PopupMenu(this, v)
        val inflater: MenuInflater = popup.menuInflater
        inflater.inflate(R.menu.menu_profile_photo, popup.menu)
        popup.show()
    }
    fun showMenu(v: View) {
        PopupMenu(this, v).apply {
            // MainActivity implements OnMenuItemClickListener
            setOnMenuItemClickListener(this@SignUpAcitivity)
            inflate(R.menu.menu_profile_photo)
            show()
        }
    }
    override fun onMenuItemClick(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.choosePhoto -> {
                val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
                startActivityForResult(gallery, pickImage)
                true
            }
            R.id.removePhoto -> {
                val uri: String = "@drawable/profile_image_default"
                var imageResource = getResources().getIdentifier(uri, null, getPackageName()) //Gets the resource using the URI
                val res = getResources().getDrawable(imageResource) //Converting the image resource into a drawable file
                profileImageView.setImageDrawable(res) //Attach/set the drawable to the Image view
                true
            }
            R.id.viewPhoto -> {
                true
            }
            else -> {
                true
            }
        }
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
    private fun authenticate(it: View){

        if(fname.text.toString().isEmpty()){
            fname.error = "Please fill this up"
            fname.requestFocus()
            return
        }
        if(lname.text.toString().isEmpty()){
            lname.error = "Please fill this up"
            lname.requestFocus()
            return
        }
        if(mobileNum.text.toString().isEmpty()){
            mobileNum.error = "Please fill this up"
            mobileNum.requestFocus()
            return
        }


       if (email.text.toString().isEmpty()){
           email.error = "Please enter email"
           email.requestFocus()
           return
       }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.text.toString()).matches()){
            email.error = "Please enter valid email"
            email.requestFocus()
            return
        }

        if (password.text.toString().isEmpty()){
            password.error = "Please enter password"
            password.requestFocus()
            return
        }

        if ((password.text.toString().isNotEmpty()) &&(password.text.toString() != retypePass.text.toString())){
            retypePass.error = "Please retype password"
            retypePass.requestFocus()
            return
        }

        auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    //Email Verification
                    val user = auth.currentUser
                    user!!.sendEmailVerification()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this, "Account successfuly created. ", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(applicationContext, LoginActivity::class.java))
                                finish()
                            }
                        }
                    //Upload details
                    uploadImageToFirebaseDatabase()
                } else {
                    Toast.makeText(baseContext, "Signup failed. Try again after some time.",
                            Toast.LENGTH_SHORT).show()
                }
            }


    }

    private fun uploadImageToFirebaseDatabase() {
        if (selectedPhotoUri != null) {
            val fileName = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")
            ref.putFile(selectedPhotoUri!!)
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {
                            saveUserToFirebaseDatabase(it.toString())
                        }
                    }
        } else if (selectedPhotoUri == null) {
            val default = "https://firebasestorage.googleapis.com/v0/b/course-project-88fec.appspot.com/o/images%2Fprofile_image_default.webp?alt=media&token=7e38e937-059b-4a54-909c-0a638d51f7d6"
            saveUserToFirebaseDatabase(default)
        }

    }

    //3 child nodes will be created. USERS, USER SELLER INFO, SKILLS
    private fun saveUserToFirebaseDatabase(profileImageUrl: String){
        val uid = auth.uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(
                uid = uid,
                emailAddress = email.text.toString(),
                firstName = fname.text.toString(),
                lastName = lname.text.toString(),
                age = age.text.toString().toInt(),
                profileImageUrl = profileImageUrl,
                mobileNumber = mobileNum.text.toString())
        ref.setValue(user)

        val anotherRef =FirebaseDatabase.getInstance().getReference("user_seller_info/$uid")
        val userSellerInfo = UserSellerInfo(
            uid = uid
        )
        anotherRef.setValue(userSellerInfo)

        val userSkillsHandler = UserSkillsHandler()

        val skill = UserSkills()
        userSkillsHandler.createSkill(skill)
    }







}

