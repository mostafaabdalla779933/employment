package com.employment.employment.feature

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.NotificationModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.databinding.FragmentEmployeeDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date

class EmployeeDetailsFragment : BaseFragment<FragmentEmployeeDetailsBinding>() {

    private val args: EmployeeDetailsFragmentArgs by navArgs()

    override fun initBinding() = FragmentEmployeeDetailsBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnInterview.setOnClickListener {
                findNavController()
                    .navigate(
                        EmployeeDetailsFragmentDirections
                            .actionEmployeeDetailsFragmentToSendInterviewDetailsFragment(
                                args.employee
                            )
                    )
            }
        }

        displayData()
    }

    private fun displayData() {
        binding.apply {
            Glide.with(requireContext())
                .load(args.employee.profileUrl)
                .placeholder(R.drawable.ic_employee)
                .into(ivEmployee)
            tvEmployeeName.text = "${args.employee.firstName} ${args.employee.lastName}"
            tvEmployeeNumber.text = args.employee.mobile
            tvEmployeeJob.text =
                args.employee.resume?.listOfQualifications?.get(0)?.qualification ?: "IT"
        }
    }


}