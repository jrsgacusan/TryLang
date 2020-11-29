package com.example.trylang


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity



class LoginActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //Map the views from the layout file
        val logInBtn = findViewById<Button>(R.id.logInBtn)
        val signUpTextView = findViewById<TextView>(R.id.signUpTextView)

        logInBtn.setOnClickListener {
            val intent = Intent(this, BuyerActivity::class.java )
            startActivity(intent)
        }
        signUpTextView.setOnClickListener {
            val intent = Intent(this, SignUpPage::class.java )
            startActivity(intent)
        }



}}