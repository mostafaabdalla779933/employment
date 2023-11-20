package com.employment.employment.feature

import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.JobModel
import com.employment.employment.databinding.FragmentCompanyJobApplicationsBinding
import com.employment.employment.feature.userDashboard.JobsAdapter
import com.employment.employment.feature.userDashboard.UserHomeFragmentDirections

class CompanyJobApplicationsFragment : BaseFragment<FragmentCompanyJobApplicationsBinding>() {

    private val jobsAdapter: JobsAdapter by lazy {
        JobsAdapter {
            findNavController().navigate(
                CompanyJobApplicationsFragmentDirections
                    .actionCompanyJobApplicationsFragmentToJobDetailsFragment2(
                        it
                    )
            )
        }
    }

    override fun initBinding() = FragmentCompanyJobApplicationsBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setActions()
    }

    private fun setActions() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
            rvJobs.adapter = jobsAdapter
        }

        getJobs()
    }

    private fun getJobs() {
        showLoading()
        FirebaseHelp.getAllObjects<JobModel>(FirebaseHelp.JOBS, { allJos ->
            hideLoading()
            jobsAdapter.submitList(allJos.filter { it.company?.userId == FirebaseHelp.getUserID() })
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }

}