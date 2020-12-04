package com.example.trylang


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.trylang.buyer_activities.BuyerActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {

    lateinit var emailEditText: EditText
    lateinit var passwordEditText: EditText


    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        auth = FirebaseAuth.getInstance()

        //Map the views from the layout file
        val logInBtn = findViewById<Button>(R.id.logInBtn)
        val signUpTextView = findViewById<TextView>(R.id.signUpTextView)
        emailEditText = findViewById(R.id.firstNameEditText_profileSettings)
        passwordEditText = findViewById(R.id.passwordEditText)

        logInBtn.setOnClickListener {
            doLogin()
        }



        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpAcitivity::class.java )
            startActivity(intent)
        }


    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }


    private fun updateUI (currentUser: FirebaseUser?){
        if (currentUser!= null) {
            if(currentUser.isEmailVerified){
                val intent = Intent(this, BuyerActivity::class.java )
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                Toast.makeText(baseContext, "Signed in",
                    Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(baseContext, "Please verify your email address.",
                    Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(baseContext, "Login again.",
                Toast.LENGTH_LONG).show()
        }
    }

    fun doLogin(){
        if (emailEditText.text.toString().isEmpty()){
            emailEditText.error = "Please enter email"
            emailEditText.requestFocus()
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(emailEditText.text.toString()).matches()){
            emailEditText.error = "Please enter valid email"
            emailEditText.requestFocus()
            return
        }

        if (passwordEditText.text.toString().isEmpty()){
            passwordEditText.error = "Please enter password"
            passwordEditText.requestFocus()
            return
        }

        auth.signInWithEmailAndPassword(emailEditText.text.toString(), passwordEditText.text.toString())
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    updateUI(null)
                }
            }




    }

}