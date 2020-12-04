package com.example.trylang.buyer_activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trylang.LoginActivity
import com.example.trylang.ProfileSettingsActivity
import com.example.trylang.R
import com.example.trylang.buyerfragments.HomeFragment
import com.example.trylang.buyerfragments.NotificationsFragment
import com.example.trylang.buyerfragments.ProfileFragment
import com.example.trylang.buyerfragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


lateinit var bottomNavigationBuyer : BottomNavigationView
class BuyerActivity : AppCompatActivity() {

    val homeFragment = HomeFragment()
    val notificationsFragment = NotificationsFragment ()
    val searchFragment = SearchFragment ()
    val profileFragment = ProfileFragment ()
    lateinit var menuItem: MenuItem



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)

        verifyUserIsLoggedIn()

        //Action bar at the top
        setSupportActionBar(findViewById(R.id.toolBar))
        //supportActionBar!!.setDisplayShowTitleEnabled(false) //Removes the title
        //Initialize home fragment
        makeCurrentFragment(ProfileFragment())
        //Map the bottom navigation view
        bottomNavigationBuyer = findViewById(R.id.bottomNavigation)
        bottomNavigationBuyer.menu.findItem(R.id.profilePage).setChecked(true)
        //Onclick listener for the bottom nav, bottom navigation menu have a corresponding id.
        bottomNavigationBuyer.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.homePage ->  {
                    makeCurrentFragment(homeFragment)
                }
                R.id.search -> {
                    makeCurrentFragment(searchFragment)
                }
                R.id.notifications -> {
                    makeCurrentFragment(notificationsFragment)
                }
                R.id.profilePage -> {
                    makeCurrentFragment(profileFragment)
                }
            }
            true
        }


    }

    //Options Menu on the upper right side
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        menuItem = menu!!.findItem(R.id.addService)
        menuItem.setVisible(false)
        return super.onCreateOptionsMenu(menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                makeCurrentFragment(searchFragment)
            }
            R.id.profileSettings -> {
                val intent = Intent(this, ProfileSettingsActivity::class.java)
                intent.putExtra("intent", "buyer")
                startActivity(intent)
            }
            R.id.logOut -> {
                //Dialog before sign out
                val dialogBuilder = AlertDialog.Builder(this)
                // set message of alert dialog
                dialogBuilder.setMessage("Do you want to sign out?")
                        // if the dialog is cancelable
                        .setCancelable(true)
                        // positive button text and action
                        .setPositiveButton("Proceed", DialogInterface.OnClickListener {
                            dialog, id ->
                            FirebaseAuth.getInstance().signOut()
                            val intent = Intent(this, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                            finish()
                            Toast.makeText(this, "Signed out", Toast.LENGTH_LONG).show()
                        })
                        // negative button text and action
                        .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                        })
                // create dialog box
                val alert = dialogBuilder.create()
                // set title for alert dialog box
                alert.setTitle("Sign Out")
                // show alert dialog
                alert.show()
            }

        }
        return super.onOptionsItemSelected(item)
    }
    //Function that will change the current fragment.
    private fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.wrapper, fragment)
            commit()
        }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }

    fun verifyUserIsLoggedIn() {

        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }




}