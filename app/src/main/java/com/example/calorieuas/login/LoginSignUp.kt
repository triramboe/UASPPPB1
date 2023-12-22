package com.example.calorieuas.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.example.calorieuas.adapter.FragmentLoginAdapter
import com.example.calorieuas.user.MainActivity
import com.example.calorieuas.admin.MainActivityAdmin
import com.example.calorieuas.SessionManager
import com.example.calorieuas.databinding.ActivityLoginSignUpBinding
import com.google.android.material.tabs.TabLayout

class LoginSignUp : AppCompatActivity() {

    private lateinit var tabLayout : TabLayout
    private lateinit var viewPager2 : ViewPager2
    private lateinit var adapter : FragmentLoginAdapter

    private lateinit var binding: ActivityLoginSignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        tabLayout = binding.loginTabLayout
        viewPager2 = binding.loginViewPager

        adapter = FragmentLoginAdapter(supportFragmentManager, lifecycle)

        tabLayout.addTab(tabLayout.newTab().setText("LOGIN"))
        tabLayout.addTab(tabLayout.newTab().setText("SIGN UP"))

        viewPager2.adapter = adapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab != null){
                    viewPager2.currentItem = tab.position
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }

            override fun onTabReselected(tab: TabLayout.Tab?) {

            }

        })

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.selectTab(tabLayout.getTabAt(position))
            }
        })

        val session = SessionManager(this)
        if (session.isLoggedIn) {
            val role = session.userRole
            if (role == "admin"){
                startActivity(Intent(this, MainActivityAdmin::class.java))
                finish()
            }
            else
            {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}