package com.employment.employment.feature.companyDashboard

import android.widget.ArrayAdapter
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.firebase.data.listOfAges
import com.employment.employment.common.firebase.data.listOfExperience
import com.employment.employment.common.firebase.data.listOfJobs
import com.employment.employment.common.firebase.data.listOfNationality
import com.employment.employment.common.firebase.data.listOfWorkTime
import com.employment.employment.databinding.FragmentCompanySearchBinding


class CompanySearchFragment : BaseFragment<FragmentCompanySearchBinding>() {

    private val adapter: EmployeesAdapter by lazy { EmployeesAdapter {} }

    override fun initBinding() = FragmentCompanySearchBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        getAllEmployees()
        initSpinners()
    }

    private fun initSpinners() {
        binding.apply {
            val arrJobs = listOfJobs.toMutableList()
            spinnerJobName.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                arrJobs
            )
            spinnerJobName.setSelection(0, false)


            val arrNationality = listOfNationality.toMutableList()
            spinnerNationality.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                arrNationality
            )
            spinnerNationality.setSelection(0, false)

            val arrAges = listOfAges.toMutableList()
            spinnerAge.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                arrAges
            )
            spinnerAge.setSelection(0, false)

            val arrExperience = listOfExperience.toMutableList()
            spinnerExperience.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                arrExperience
            )
            spinnerExperience.setSelection(0, false)

            val arrWorkTime = listOfWorkTime.toMutableList()
            spinnerJobType.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                arrWorkTime
            )
            spinnerJobType.setSelection(0, false)
        }
    }

    private fun getAllEmployees() {
        showLoading()
        FirebaseHelp.getAllObjects<UserModel>(FirebaseHelp.USERS, { allUsers ->
            hideLoading()
            binding.rvEmployees.adapter = adapter
            adapter.submitList(allUsers?.filter { e -> e.userType == UserType.User.value })
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }
}