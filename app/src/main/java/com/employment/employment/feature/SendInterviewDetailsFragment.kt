package com.employment.employment.feature

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.view.Window
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragmentDialog
import com.employment.employment.common.base.DateFragment
import com.employment.employment.common.base.DateFragmentMax
import com.employment.employment.common.base.DateFragmentMin
import com.employment.employment.common.base.TimeFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.NotificationModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentSendInterviewDetailsBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class SendInterviewDetailsFragment : BaseFragmentDialog<FragmentSendInterviewDetailsBinding>(),
    TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private var selectedStartTime: String? = null
    private var selectedStartDate: Date? = null

    private val args: SendInterviewDetailsFragmentArgs by navArgs()

    override fun initBinding() = FragmentSendInterviewDetailsBinding.inflate(layoutInflater)

    override fun onDialogCreated() {
        isCancelable = true

        binding.apply {

            etTime.setOnClickListener {
                TimeFragment(this@SendInterviewDetailsFragment).also {
                    it.dialog?.window?.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY)
                }.show(parentFragmentManager, "time")
            }

            etDate.setOnClickListener {
                DateFragmentMin(this@SendInterviewDetailsFragment).also {
                    it.dialog?.window?.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                }.show(parentFragmentManager, "date")
            }

            btnSend.setOnClickListener {
                validate()
            }
        }
    }

    override fun getTheme(): Int {
        super.getTheme()
        return R.style.DialogStyle
    }

    private fun validate() {
        if (binding.etTime.text.toString().isEmpty()) {
            requireContext().showMessage("You must enter interview time")
        } else if (binding.etDate.text.toString().isEmpty()) {
            requireContext().showMessage("You must enter interview date")
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
            from = FirebaseHelp.user,
            type = UserType.Company.value,
            toUserId = args.employe.userId,
            interviewTime = selectedStartTime,
            interviewTDate = selectedStartDate.toString()
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

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        selectedStartTime = SimpleDateFormat("hh:mm a").format(Date(calendar.timeInMillis))
        binding.etTime.text = selectedStartTime
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        binding.apply {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            selectedStartDate = Date(calendar.timeInMillis)
            val date = SimpleDateFormat("dd/MM/yyyy").format(selectedStartDate)
            etDate.text = date
        }
    }

}