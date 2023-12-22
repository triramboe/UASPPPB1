package com.example.calorieuas.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.calorieuas.login.LoginFragment
import com.example.calorieuas.login.SignUpFragment

class FragmentLoginAdapter (fragmetManager :FragmentManager, lifecycle: Lifecycle):
        FragmentStateAdapter(fragmetManager, lifecycle){
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if(position == 0)
            LoginFragment()
        else
            SignUpFragment()
    }
        }