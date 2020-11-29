package com.example.trylang

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.trylang.sellerfragments.SellerManageFragment
import com.example.trylang.sellerfragments.SellerNotificationsFragment
import com.example.trylang.sellerfragments.SellerProfileFragment
import com.example.trylang.sellerfragments.SellerServicesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

lateinit var bottomNavigationSeller : BottomNavigationView




class SellerActivity : AppCompatActivity() {

    val sellerManage = SellerManageFragment()
    val sellerNotifications = SellerNotificationsFragment()
    val sellerProfile = SellerProfileFragment()
    val sellerServices = SellerServicesFragment()




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
                    toolBar.menu.findItem(R.id.addService).setVisible(true)
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
    //Options Menu on the upper right side
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_main, menu)

        val item = menu!!.findItem(R.id.search)
        item.setVisible(false)

        return super.onCreateOptionsMenu(menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.addService -> {

            }
            R.id.profileSettings -> {
            }
            R.id.logOut -> {
            }

        }
        return super.onOptionsItemSelected(item)
    }



    //Function that will change the current fragment.
    public fun makeCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply{
            replace(R.id.wrapper, fragment)
            commit()
        }

    fun setActionBarTitle(title: String?) {
        supportActionBar!!.title = title
    }



}