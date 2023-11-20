package com.employment.employment.feature.createJobApp

import android.app.TimePickerDialog
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.TimePicker
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.base.TimeFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.JobModel
import com.employment.employment.common.firebase.data.listOfAges
import com.employment.employment.common.firebase.data.listOfExperience
import com.employment.employment.common.firebase.data.listOfJobs
import com.employment.employment.common.firebase.data.listOfNationality
import com.employment.employment.common.firebase.data.listOfWorkTime
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentCreateJobApplicationBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class CreateJobApplicationFragment : BaseFragment<FragmentCreateJobApplicationBinding>(),
    TimePickerDialog.OnTimeSetListener {

    private var isStartTime = true
    private var selectedStartTime: String? = null
    private var selectedEndTime: String? = null

    override fun initBinding() = FragmentCreateJobApplicationBinding.inflate(layoutInflater)
    override fun onFragmentCreated() {
        setActions()
    }

    private fun setActions() {

        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            btnNewJob.setOnClickListener {
                validate()
            }

            etFromHour.setOnClickListener {
                isStartTime = true
                TimeFragment(this@CreateJobApplicationFragment).also {
                    it.dialog?.window?.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                }.show(parentFragmentManager, "time")
            }

            etToHour.setOnClickListener {
                if (selectedStartTime == null) {
                    showErrorMsg("select start time first")
                } else {
                    isStartTime = false
                    TimeFragment(this@CreateJobApplicationFragment).also {
                        it.dialog?.window?.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                    }.show(parentFragmentManager, "time")
                }
            }
        }

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
            spinnerWorkTime.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                arrWorkTime
            )
            spinnerWorkTime.setSelection(0, false)
        }
    }

    private fun validate() {
        binding.apply {
            when {
                spinnerJobName.selectedItem.toString().isEmpty() -> {
                    showErrorMsg("select job")
                }

                etJobLocation.isStringEmpty() -> {
                    showErrorMsg("fill job Location")
                }

                etJobDescription.isStringEmpty() -> {
                    showErrorMsg("fill job Description")
                }

                etJobConditions.isStringEmpty() -> {
                    showErrorMsg("fill job Conditions")
                }

                etJobRequirement.isStringEmpty() -> {
                    showErrorMsg("fill job Requirement")
                }

                selectedStartTime == null -> {
                    showErrorMsg("select start time")
                }

                selectedEndTime == null -> {
                    showErrorMsg("select End time")
                }

                spinnerNationality.selectedItem.toString().isEmpty() -> {
                    showErrorMsg("select Nationality")
                }

                spinnerAge.selectedItem.toString().isEmpty() -> {
                    showErrorMsg("select Age")
                }

                spinnerExperience.selectedItem.toString().isEmpty() -> {
                    showErrorMsg("select Experience")
                }

                spinnerWorkTime.selectedItem.toString().isEmpty() -> {
                    showErrorMsg("select Work Time")
                }

                else -> {
                    createJobApplication()
                }
            }
        }
    }

    private fun createJobApplication() {
        binding.apply {
            showLoading()
            val job = JobModel(
                hashed = System.currentTimeMillis().toString(),
                name = spinnerJobName.selectedItem.toString(),
                location = etJobLocation.getString(),
                desc = etJobDescription.getString(),
                conditions = etJobConditions.getString(),
                requirement = etJobRequirement.getString(),
                salary = etJobSalary.getString(),
                workingFrom = selectedStartTime,
                workingTo = selectedEndTime,
                vacations = etJobVacations.getString(),
                nationality = spinnerNationality.selectedItem.toString(),
                age = spinnerAge.selectedItem.toString(),
                experience = spinnerExperience.selectedItem.toString(),
                jobType = spinnerWorkTime.selectedItem.toString(),
                company = FirebaseHelp.user
            )

            FirebaseHelp.addObject<JobModel>(
                job,
                FirebaseHelp.JOBS,
                job.hashed ?: "", {
                    hideLoading()
                    requireContext().showMessage("success")
                    findNavController().popBackStack()
                }, {
                    hideLoading()
                    showErrorMsg(it)
                })
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {

        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)

        val date = SimpleDateFormat("hh:mm:a").format(Date(calendar.timeInMillis))
        if (isStartTime) {
            selectedStartTime = date.replace(":a", "a")
            binding.apply {
                etFromHour.setText(selectedStartTime)
                selectedEndTime = null
            }
        } else {
            selectedEndTime = date.replace(":a", "a")
            if (!isValidTime()) {
                requireContext().showMessage("please choose valid period")
            } else {
                binding.apply {
                    etToHour.setText(selectedEndTime)
                }
            }
        }
    }

    private fun isValidTime(): Boolean {
        val start = SimpleDateFormat("HH:mm:ss").parse(selectedStartTime?.get24())
        val end = SimpleDateFormat("HH:mm:ss").parse(selectedEndTime?.get24())

        return start.before(end) && start.compareTo(end) != 0
    }

    private fun String.get24(): String {
        return try {
            val inputFormat = SimpleDateFormat("hh:mm:a", Locale.US)
            val outputFormat = SimpleDateFormat("HH:mm:ss", Locale.US)
            outputFormat.format(inputFormat.parse(this))
        } catch (e: Exception) {
            ""
        }
    }

}