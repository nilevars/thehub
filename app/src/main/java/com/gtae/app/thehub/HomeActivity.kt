package com.gtae.app.thehub

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.BottomNavigationView
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)



        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.visibility = View.GONE




        home_icon.setColorFilter(resources.getColor(R.color.colorAccent))
       // val myf = HomePage()
        val transaction = supportFragmentManager.beginTransaction()
      //  transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
      //  transaction.add(R.id.content, myf)
      //  transaction.commit()

        /*comm.setOnClickListener {
            setBlack()
            comm.setColorFilter(resources.getColor(R.color.blue_400))
            val myf2 = CommunityFragment()
            val transaction = supportFragmentManager.beginTransaction().addToBackStack(null)
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            transaction.add(R.id.content, myf2)
            transaction.commit()

            fab.visibility = View.VISIBLE
            fab.setOnClickListener {
                val intent = Intent(this@HomeActivity, CreateCommunityActivity::class.java)
                startActivity(intent)
            }
        }
        home.setOnClickListener {
            setBlack()
            home.setColorFilter(resources.getColor(R.color.pink_400))
            val myf = HomePage()
            val transaction = supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            transaction.add(R.id.content, myf)
            transaction.commit()

            fab.visibility = View.VISIBLE
            fab.setOnClickListener {
                val intent = Intent(this@HomeActivity, CreateCommunityActivity::class.java)
                startActivity(intent)
            }
        }
        user.setOnClickListener {
            setBlack()
            user.setColorFilter(resources.getColor(R.color.red_300))
            val myf = UserDetails()
            val transaction = supportFragmentManager.beginTransaction().addToBackStack(null)
           // transaction.setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            transaction.add(R.id.content, myf)
            transaction.commit()
        }*/


    }

    override fun onBackPressed() {
        if (fragmentManager.backStackEntryCount == 1) {
            finish()
        } else {
            super.onBackPressed()
        }
    }

    internal fun setBlack() {
        home_icon.setColorFilter(resources.getColor(R.color.black))
    }

}
