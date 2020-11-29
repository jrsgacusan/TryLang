package com.example.trylang

import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

class SignUpPage : AppCompatActivity(), PopupMenu.OnMenuItemClickListener {

    lateinit var profileImageView: ImageView
    lateinit var uploadImageView: ImageView
    private val pickImage = 100
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_page)

        //Map the layout file views to the Kotlin file
        val fname = findViewById<EditText>(R.id.firstNameEditText)
        val lname = findViewById<EditText>(R.id.lastNameEditText)
        val email = findViewById<EditText>(R.id.emailAddEditText)
        val mobileNum = findViewById<EditText>(R.id.mobileNumberEditText)
        val password = findViewById<EditText>(R.id.retypePasswordEditText)
        val retypePass = findViewById<EditText>(R.id.retypePasswordEditText)
        val age = findViewById<TextView>(R.id.ageTextView)
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
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
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
            //If there is missing information
            if (fname.text.isEmpty() ||
                lname.text.isEmpty() ||
                email.text.isEmpty()||
                mobileNum.text.isEmpty() ||
                password.text.isEmpty() ||
                retypePass.text.isEmpty() ){
                val snackbar = Snackbar.make(it, "Please fill up all the informations needed to proceed", Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("Okay", View.OnClickListener { //Lamda function
                    snackbar.dismiss()
                    startActivity(intent)
                })
//                Toast.makeText(this, "Please fill up all the information needed to proceed.", Toast.LENGTH_LONG).show()
            //If there is no missing information but the password doesn't match
            } else if ((fname.text.isNotEmpty() &&
                    lname.text.isNotEmpty() &&
                    email.text.isNotEmpty() &&
                    mobileNum.text.isNotEmpty() &&
                    password.text.isNotEmpty() &&
                    retypePass.text.isNotEmpty()) &&
                    (retypePass.text != password.text)
                    ) {
                val snackbar = Snackbar.make(it, "Password doesn't match", Snackbar.LENGTH_INDEFINITE)
                snackbar.setAction("Okay", View.OnClickListener { //Lamda function
                    snackbar.dismiss()
                    startActivity(intent)
                })
                snackbar.show()
//                Toast.makeText(this, "Password doesn't match.", Toast.LENGTH_LONG).show()
            //If everything is okay
            } else {
                val dialogBuilder = AlertDialog.Builder(this)
                dialogBuilder.setMessage("Sign up now?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which ->
                            //Register the account and go to the Login Activity
                            val intent = Intent(this, LoginActivity::class.java)
                            Toast.makeText(this, "Your account has been registered.", Toast.LENGTH_LONG).show()
                            startActivity(intent)
                        })
                        .setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->
                            dialog.cancel()
                        })
                val alert = dialogBuilder.create()
                alert.setTitle("Notifcation Dialog")
                alert.show()
            }
        }


    }
    //Uploading an image to image view override function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == pickImage) {
            imageUri = data?.data
            profileImageView.setImageURI(imageUri)
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
            setOnMenuItemClickListener(this@SignUpPage)
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
                var res = getResources().getDrawable(imageResource) //Converting the image resource into a drawable file
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




}