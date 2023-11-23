package com.employment.employment.feature

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.NotificationModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.databinding.FragmentJobDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date

class JobDetailsFragment : BaseFragment<FragmentJobDetailsBinding>() {

    private val args: JobDetailsFragmentArgs by navArgs()

    override fun initBinding() = FragmentJobDetailsBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        displayData()
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnRequest.setOnClickListener {
                userSendNotificationRequestToCompany()
            }
        }
    }

    private fun displayData(){
        binding.apply {
            tvScreenTittle.text = args.job.company?.name
            tvJobTitle.text = args.job.name
            tvJobLocation.text = args.job.location
            tvJobDescription.text = args.job.desc
            tvJobConditions.text = args.job.conditions
            tvJobRequirement.text = args.job.requirement
            tvJobSalary.text = if(args.job.salary.isNullOrEmpty()) "Unavailable" else args.job.salary
            tvJobHours.text = "${args.job.workingFrom} - ${args.job.workingTo}"
            tvJobVacations.text = if(args.job.vacations.isNullOrEmpty()) "Unavailable" else args.job.vacations
            tvJobNationality.text = args.job.nationality
            tvJobAge.text = args.job.age
            tvJobExperience.text = args.job.experience
            tvJobWorkTime.text = args.job.jobType
        }
    }

    private fun userSendNotificationRequestToCompany() {
        showLoading()
        val sdf = SimpleDateFormat("hh:mm a dd/MM/yyyy ")
        val currentDate = sdf.format(Date())

        val notificationModel = NotificationModel(
            message = "${FirebaseHelp.user?.name} send you request to work.",
            hash = System.currentTimeMillis().toString(),
            date = currentDate,
            fromId = FirebaseHelp.getUserID(),
            from= FirebaseHelp.user,
            type = UserType.User.value,
            toUserId = args.job.company?.userId
        )

        FirebaseHelp.addObject<NotificationModel>(
            notificationModel,
            FirebaseHelp.NOTIFICATION,
            notificationModel.hash ?: "",
            {
                hideLoading()
                showMessage("success")
                findNavController().popBackStack()
            },
            {
                hideLoading()
            })
    }

}