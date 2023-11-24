package com.employment.employment.feature.companyDashboard


import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.JobModel
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.databinding.FragmentCompanyHomeBinding
import com.google.android.material.tabs.TabLayout


class CompanyHomeFragment : BaseFragment<FragmentCompanyHomeBinding>() {

    private var users : MutableList<UserModel>? = null
    private var recommended = mutableListOf<UserModel>()

    private val adapter : EmployeesAdapter by lazy {
        EmployeesAdapter{
            findNavController().navigate(
                CompanyHomeFragmentDirections
                    .actionCompanyHomeFragmentToEmployeeDetailsFragment(it)
            )
        }
    }
    override fun initBinding() = FragmentCompanyHomeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {
            ivNotification.setOnClickListener {
                findNavController().navigate(CompanyHomeFragmentDirections.actionCompanyHomeFragmentToNotificationFragment())
            }
        }
        addTabListener()
        getAllEmployees()
        getJobs()
    }


    private fun getAllEmployees() {
        showLoading()
        FirebaseHelp.getAllObjects<UserModel>(FirebaseHelp.USERS, { allUsers ->
            hideLoading()
            users = allUsers.filter { e -> e.userType == UserType.User.value && e.resume != null}.toMutableList()
            adapter.submitList(users)
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }

    private fun getJobs() {
        FirebaseHelp.getAllObjects<JobModel>(FirebaseHelp.JOBS, { allJos ->
            users?.let {
                for (job in allJos) {
                    for (user in it) {
                        if (job.name == user.resume?.listOfExperiences?.get(0)?.jobTitle
                            || job.nationality == user.resume?.nationality ) {
                            recommended.add(user)
                        }
                    }
                }
            }

        }, {
        })
    }

    private fun addTabListener(){
        binding.rvEmployees.adapter = adapter
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when(tab?.position){
                    0 ->{
                        adapter.submitList(users)
                    }
                    1 ->{
                        adapter.submitList(recommended)
                    }
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })
    }
}



