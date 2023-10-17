package com.employment.employment.feature.companyDashboard


import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.databinding.FragmentCompanyHomeBinding
import com.google.android.material.tabs.TabLayout


class CompanyHomeFragment : BaseFragment<FragmentCompanyHomeBinding>() {

    private var users : MutableList<UserModel>? = null
    private val adapter : EmployeesAdapter by lazy {
        EmployeesAdapter{

        }
    }
    override fun initBinding() = FragmentCompanyHomeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        addTabListener()
        getAllEmployees()
    }


    private fun getAllEmployees() {
        showLoading()
        FirebaseHelp.getAllObjects<UserModel>(FirebaseHelp.USERS, { allUsers ->
            users = allUsers
            hideLoading()
            users?.let {
                adapter.submitList(users?.filter { e -> e.userType == UserType.User.value })
            }
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }

    private fun addTabListener(){
        binding.rvEmployees.adapter = adapter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 ->{
                        showErrorMsg("0")
                    }
                    1 ->{
                        showErrorMsg("1")
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}



