package com.employment.employment.feature.companyDashboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.employment.employment.R
import com.employment.employment.databinding.ActivityCompanyDashboardBinding

class CompanyDashboardActivity : AppCompatActivity() {

    lateinit var binding:ActivityCompanyDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCompanyDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initBottomNavigation()

    }

    private fun initBottomNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_company_activity_host) as NavHostFragment
        val navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navView, navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when (destination.id) {
                R.id.companyHomeFragment, R.id.companyMenuFragment,
                R.id.companySearchFragment -> {
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