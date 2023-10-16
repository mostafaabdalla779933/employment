package com.employment.employment.feature.userDashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.employment.employment.R
import com.employment.employment.databinding.ActivityUserDashboardBinding

class UserDashboardActivity : AppCompatActivity() {


    lateinit var binding:ActivityUserDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigation()
    }


    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_user_activity_host) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.userHomeFragment, R.id.userMenuFragment,
                R.id.userResumeFragment -> {
                    binding.navView.visibility = View.VISIBLE
                }
                else -> {
                    binding.navView.visibility = View.GONE

                }

            }
        }
        binding.navView.setOnItemReselectedListener { }

    }
}