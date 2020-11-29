package com.example.trylang

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.trylang.buyerfragments.HomeFragment
import com.example.trylang.buyerfragments.NotificationsFragment
import com.example.trylang.buyerfragments.ProfileFragment
import com.example.trylang.buyerfragments.SearchFragment
import com.google.android.material.bottomnavigation.BottomNavigationView


lateinit var bottomNavigationBuyer : BottomNavigationView
class BuyerActivity : AppCompatActivity() {

    val homeFragment = HomeFragment()
    val notificationsFragment = NotificationsFragment ()
    val searchFragment = SearchFragment ()
    val profileFragment = ProfileFragment ()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buyer)

        //Action bar at the top
        setSupportActionBar(findViewById(R.id.toolBar))
        //supportActionBar!!.setDisplayShowTitleEnabled(false) //Removes the title
        //Initialize home fragment
        makeCurrentFragment(ProfileFragment())
        //Map the bottom navigation view
        bottomNavigationBuyer = findViewById<BottomNavigationView>(R.id.bottomNavigation)
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
        val item = menu!!.findItem(R.id.addService)
        item.setVisible(false)
        return super.onCreateOptionsMenu(menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.search -> {
                makeCurrentFragment(searchFragment)
            }
            R.id.profileSettings -> {}
            R.id.logOut -> {}

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





}