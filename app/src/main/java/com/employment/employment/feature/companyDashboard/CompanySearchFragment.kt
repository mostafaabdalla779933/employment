package com.employment.employment.feature.companyDashboard

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.calculateAge
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.firebase.data.listOfAges
import com.employment.employment.common.firebase.data.listOfExperience
import com.employment.employment.common.firebase.data.listOfJobs
import com.employment.employment.common.firebase.data.listOfNationality
import com.employment.employment.common.firebase.data.listOfWorkTime
import com.employment.employment.common.getAgeRange
import com.employment.employment.common.getExperienceRange
import com.employment.employment.databinding.FragmentCompanySearchBinding


class CompanySearchFragment : BaseFragment<FragmentCompanySearchBinding>() {

    private val adapter: EmployeesAdapter by lazy { EmployeesAdapter {} }

    var users = mutableListOf<UserModel>()

    override fun initBinding() = FragmentCompanySearchBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        getAllEmployees()
        initSpinners()

        binding.ivNotification.setOnClickListener {
            findNavController().navigate(
                CompanySearchFragmentDirections
                    .actionCompanySearchFragmentToNotificationFragment()
            )
        }
    }

    private fun initSpinners() {
        binding.apply {

            spinnerJobName.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_new,
                listOfJobs.toTypedArray()
            )

            spinnerNationality.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_new,
                listOfNationality.toTypedArray()
            )

            spinnerAge.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_new,
                listOfAges.toMutableList()
            )

            spinnerExperience.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_new,
                listOfExperience.toMutableList()
            )

            spinnerJobType.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item_new,
                listOfWorkTime.toMutableList()
            )


            spinnerJobName.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    filterList()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            spinnerNationality.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    filterList()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            spinnerAge.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    filterList()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }

            spinnerExperience.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    filterList()
                }
                override fun onNothingSelected(p0: AdapterView<*>?) {}
            }
        }
    }

    private fun getAllEmployees() {
        showLoading()
        FirebaseHelp.getAllObjects<UserModel>(FirebaseHelp.USERS, { allUsers ->
            hideLoading()
            binding.rvEmployees.adapter = adapter
            users = (allUsers.filter { e -> e.userType == UserType.User.value && e.resume != null } ?: mutableListOf()).toMutableList()
            filterList()
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }

    private fun filterList(){

        binding.apply {

            adapter.submitList( users.filter { e -> e.resume?.jobTitle ==  spinnerJobName.selectedItem.toString()
                    && getAgeRange(e.resume?.birthDate?.calculateAge() ?: 0) ==spinnerAge.selectedItem.toString()
                    && e.resume?.nationality == spinnerNationality.selectedItem.toString()
                    && if(spinnerExperience.selectedItemPosition == 4) true else e.resume?.listOfExperiences?.sumOf { e -> e?.experience ?: 0L }?.getExperienceRange() == spinnerExperience.selectedItem.toString()
            })

        }

    }
}