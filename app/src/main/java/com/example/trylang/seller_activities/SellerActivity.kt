package com.example.trylang.seller_activities

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
import com.example.trylang.sellerfragments.SellerManageFragment
import com.example.trylang.sellerfragments.SellerNotificationsFragment
import com.example.trylang.sellerfragments.SellerProfileFragment
import com.example.trylang.sellerfragments.SellerServicesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

lateinit var bottomNavigationSeller : BottomNavigationView




class SellerActivity : AppCompatActivity() {

    val sellerManage = SellerManageFragment()
    val sellerNotifications = SellerNotificationsFragment()
    val sellerProfile = SellerProfileFragment()
    val sellerServices = SellerServicesFragment()
    lateinit var menuItem: MenuItem
    lateinit var menuToHide: MenuItem





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seller)


        val toolBar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolBar)




        //Action bar at the top
        setSupportActionBar(toolBar)
        //supportActionBar!!.setDisplayShowTitleEnabled(false) //Removes the title

        //Initialize Profile page of the seller fragment
        makeCurrentFragment(SellerProfileFragment())

        //Map the bottom navigation view
        bottomNavigationSeller = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        //Onclick listener for the bottom nav, bottom navigation menu have a corresponding id.
        bottomNavigationSeller.setOnNavigationItemSelectedListener {
            when(it.itemId) {
                R.id.Seller_servicesPage -> {
                    makeCurrentFragment(sellerServices)
                }
                R.id.Seller_manageOrdersPage -> {
                    makeCurrentFragment(sellerManage)
                }
                R.id.Seller_notificationsPage -> {
                    makeCurrentFragment(sellerNotifications)
                }
                R.id.Seller_profilePage -> {
                    makeCurrentFragment(sellerProfile)
                }
            }
            true
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)
        menuItem = menu!!.findItem(R.id.search)
        menuToHide = menu.findItem(R.id.addService)
        menuItem.setVisible(false)
        menuToHide.setVisible(false)

        return super.onCreateOptionsMenu(menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addService -> {
                val intent = Intent(this, CreateServicesActivity::class.java)
                startActivity(intent)
            }
            R.id.profileSettings -> {
                val intent = Intent(this, ProfileSettingsActivity::class.java)
                intent.putExtra("intent", "seller")
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


    public fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.wrapper, fragment)
            commit()
        }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }





}