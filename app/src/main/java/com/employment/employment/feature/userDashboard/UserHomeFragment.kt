package com.employment.employment.feature.userDashboard


import android.view.View
import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.databinding.FragmentUserHomeBinding
import com.google.android.material.tabs.TabLayout


class UserHomeFragment : BaseFragment<FragmentUserHomeBinding>() {

    private var users : MutableList<UserModel>? = null

    private val adapter : CompaniesAdapter by lazy {
        CompaniesAdapter{
            findNavController().navigate(UserHomeFragmentDirections.actionUserHomeFragmentToCompanyDetailsFragment(it))
        }
    }
    override fun initBinding()= FragmentUserHomeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        getAllCompanies()
        addTabListener()
    }


    private fun getAllCompanies() {
        showLoading()
        FirebaseHelp.getAllObjects<UserModel>(FirebaseHelp.USERS, { allUsers ->
            users = allUsers
            hideLoading()
            users?.let {
                adapter.submitList(users?.filter { e -> e.userType == UserType.Company.value })
            }
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }

    private fun addTabListener(){
        binding.apply {
            rvCompanies.adapter = adapter
            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    when (tab?.position) {
                        0 -> {
                            rvCompanies.visibility = View.VISIBLE
                            rvJobs.visibility = View.GONE
                        }
                        1 -> {
                            rvCompanies.visibility = View.GONE
                            rvJobs.visibility = View.VISIBLE
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }


}