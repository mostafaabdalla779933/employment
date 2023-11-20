package com.employment.employment.feature.resume


import android.app.DatePickerDialog
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragmentDialog
import com.employment.employment.common.base.DateFragmentMax
import com.employment.employment.common.calculateDateDifference
import com.employment.employment.common.calculateDateDifferenceToLong
import com.employment.employment.common.firebase.data.ExperienceModel
import com.employment.employment.common.firebase.data.listOfJobs
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentAddExperienceBinding
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddExperienceFragment : BaseFragmentDialog<FragmentAddExperienceBinding>(),
    DatePickerDialog.OnDateSetListener {


    private var selectedStartDate: Date? = null
    private var selectedEndDate: Date? = null
    private var isStartDate = true

    override fun initBinding() = FragmentAddExperienceBinding.inflate(layoutInflater)

    override fun onDialogCreated() {
        isCancelable = false

        binding.apply {
            ivClose.setOnClickListener {
                findNavController().popBackStack()
            }

            spinnerJobTitle.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                listOfJobs.toTypedArray()
            )

            llStartDate.setOnClickListener {
                isStartDate = true
                cbPresentDate.isChecked = false
                DateFragmentMax(this@AddExperienceFragment).also {
                    it.dialog?.window?.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                }.show(parentFragmentManager, "date")
            }


            llEndDate.setOnClickListener {
                if (selectedStartDate == null) {
                    requireContext().showMessage("select start date first")
                } else {
                    isStartDate = false
                    cbPresentDate.isChecked = false
                    DateFragmentMax(this@AddExperienceFragment).also {
                        it.dialog?.window?.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                    }.show(parentFragmentManager, "date")
                }
            }

            cbPresentDate.setOnCheckedChangeListener { compoundButton, checked ->
                if (checked) {
                    selectedEndDate = null
                    tvDayPickerToDate.text = ""
                    tvMonthPickerToDate.text = ""
                    tvYearPickerToDate.text = ""

                    if (selectedStartDate != null) {
                        etExperienceYears.text = calculateDateDifference(
                            selectedStartDate,
                            Date(System.currentTimeMillis())
                        )
                    }

                } else {
                    etExperienceYears.text = ""
                }
            }


            btnAdd.setOnClickListener {
                validate()
            }

        }
    }

    private fun validate() {
        binding.apply {
            when {
                etCompanyName.isStringEmpty() -> {
                    requireContext().showMessage("fill Company Name")
                }

                etAboutCompany.isStringEmpty() -> {
                    requireContext().showMessage("fill Company About")
                }

                etCompanyEmail.isStringEmpty() -> {
                    requireContext().showMessage("fill Company Email")
                }

                selectedStartDate == null -> {
                    requireContext().showMessage("fill join date")
                }

                !cbPresentDate.isChecked && selectedEndDate == null -> {
                    requireContext().showMessage("fill end join date")
                }

                cbPresentDate.isChecked && calculateDateDifference(
                    selectedStartDate,
                    Date(System.currentTimeMillis())
                ).isEmpty() -> {
                    requireContext().showMessage("please choose valid period at least one month")
                }

                etMobile.isStringEmpty() -> {
                    requireContext().showMessage("fill mobile")
                }

                else -> {
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        Experience,
                        ExperienceModel(
                            jobTitle = spinnerJobTitle.selectedItem.toString(),
                            companyName = etCompanyName.getString(),
                            companyWebsite = etCompanyWebsite.getString(),
                            companyNumber = etCompanyNumber.getString(),
                            companyAbout = etAboutCompany.getString(),
                            companyEmail = etCompanyEmail.getString(),
                            startDate = selectedStartDate.toString(),
                            endDate = selectedEndDate.toString(),
                            present = cbPresentDate.isChecked,
                            mobile = etMobile.getString(),
                            experience = calculateDateDifferenceToLong(
                                selectedStartDate,
                                selectedEndDate ?: Date(System.currentTimeMillis())
                            )
                        )
                    )
                    findNavController().popBackStack()
                }
            }
        }

    }

    override fun getTheme(): Int {
        super.getTheme()
        return R.style.DialogStyle
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        binding.apply {
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)
            if (isStartDate) {
                selectedStartDate = Date(calendar.timeInMillis)
                val date = SimpleDateFormat("dd/MM/yyyy").format(selectedStartDate)
                tvDayPickerFromDate.text = date.split("/")[0]
                tvMonthPickerFromDate.text = date.split("/")[1]
                tvYearPickerFromDate.text = date.split("/")[2]

                selectedEndDate = null
                tvDayPickerToDate.text = ""
                tvMonthPickerToDate.text = ""
                tvYearPickerToDate.text = ""
                etExperienceYears.text = ""

            } else {
                selectedEndDate = Date(calendar.timeInMillis)
                if (selectedStartDate?.after(selectedEndDate) == true
                    || selectedStartDate?.compareTo(selectedEndDate) == 0
                    || calculateDateDifference(selectedStartDate, selectedEndDate).isEmpty()
                ) {
                    requireContext().showMessage("please choose valid period at least one month")
                } else {
                    val date = SimpleDateFormat("dd/MM/yyyy").format(selectedEndDate)
                    tvDayPickerToDate.text = date.split("/")[0]
                    tvMonthPickerToDate.text = date.split("/")[1]
                    tvYearPickerToDate.text = date.split("/")[2]

                    etExperienceYears.text =
                        calculateDateDifference(selectedStartDate, selectedEndDate)
                }
            }
        }
    }

    companion object {
        const val Experience = "Experience"
    }


}