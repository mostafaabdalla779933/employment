package com.employment.employment.feature.userDashboard


import android.view.View
import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.calculateAge
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.FirebaseHelp.user
import com.employment.employment.common.firebase.data.JobModel
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.firebase.data.listOfExperience
import com.employment.employment.common.getAgeRange
import com.employment.employment.common.getExperienceRange
import com.employment.employment.databinding.FragmentUserHomeBinding
import com.google.android.material.tabs.TabLayout


class UserHomeFragment : BaseFragment<FragmentUserHomeBinding>() {

    private var users: MutableList<UserModel>? = null

    private val companiesAdapter: CompaniesAdapter by lazy {
        CompaniesAdapter {
            findNavController().navigate(
                UserHomeFragmentDirections.actionUserHomeFragmentToCompanyDetailsFragment(
                    it
                )
            )
        }
    }

    private val jobsAdapter: JobsAdapter by lazy {
        JobsAdapter {
            findNavController().navigate(
                UserHomeFragmentDirections
                    .actionUserHomeFragmentToJobDetailsFragment(
                        it
                    )
            )
        }
    }

    override fun initBinding() = FragmentUserHomeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.ivNotification.setOnClickListener {
            findNavController().navigate(UserHomeFragmentDirections.actionUserHomeFragmentToNotificationFragment())
        }
        getAllCompanies()
        getAllJobs()
        addTabListener()
    }


    private fun getAllCompanies() {
        showLoading()
        FirebaseHelp.getAllObjects(FirebaseHelp.USERS, { allUsers ->
            users = allUsers
            hideLoading()
            users?.let {
                companiesAdapter.submitList(users?.filter { e -> e.userType == UserType.Company.value })
            }
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }

    private fun getAllJobs() {
        showLoading()
        FirebaseHelp.getAllObjects<JobModel>(FirebaseHelp.JOBS, { allJos ->
            hideLoading()
            jobsAdapter.submitList(allJos.filter { job ->
                job.name == user?.resume?.jobTitle
                        && getAgeRange(user?.resume?.birthDate?.calculateAge() ?: 0) == job.age
                        && job.nationality == user?.resume?.nationality
                        && if (job.experience == listOfExperience[4]) true else job.experience == user?.resume?.listOfExperiences?.sumOf { e ->
                    e?.experience ?: 0L
                }?.getExperienceRange()


            })
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }

    private fun addTabListener() {
        binding.apply {
            rvCompanies.adapter = companiesAdapter
            binding.rvJobs.adapter = jobsAdapter
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