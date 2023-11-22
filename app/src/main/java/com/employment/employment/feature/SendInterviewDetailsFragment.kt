package com.employment.employment.feature

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragmentDialog
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.NotificationModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentSendInterviewDetailsBinding
import java.text.SimpleDateFormat
import java.util.Date

class SendInterviewDetailsFragment : BaseFragmentDialog<FragmentSendInterviewDetailsBinding>() {

    private val args: SendInterviewDetailsFragmentArgs by navArgs()

    override fun initBinding() = FragmentSendInterviewDetailsBinding.inflate(layoutInflater)

    override fun onDialogCreated() {
        isCancelable = false

        binding.apply {
            btnSend.setOnClickListener {
                validate()
            }
        }
    }

    override fun getTheme(): Int {
        super.getTheme()
        return R.style.DialogStyle
    }

    private fun validate(){
        if (binding.etDetails.isStringEmpty()){
            requireContext().showMessage("You must enter interview time")
        } else {
            companySendNotificationRequestToUser()
        }
    }

    private fun companySendNotificationRequestToUser() {
        val sdf = SimpleDateFormat("hh:mm a dd/MM/yyyy ")
        val currentDate = sdf.format(Date())

        val notificationModel = NotificationModel(
            message = "${FirebaseHelp.user?.name} send you request to interview.",
            hash = System.currentTimeMillis().toString(),
            date = currentDate,
            fromId = FirebaseHelp.getUserID(),
            from= FirebaseHelp.user,
            type = UserType.Company.value,
            toUserId = args.employe.userId,
            interviewTime = binding.etDetails.getString()
        )

        FirebaseHelp.addObject<NotificationModel>(
            notificationModel,
            FirebaseHelp.NOTIFICATION,
            notificationModel.hash ?: "",
            {
                requireContext().showMessage("success")
                findNavController().popBackStack()
            },
            {

            })
    }

}