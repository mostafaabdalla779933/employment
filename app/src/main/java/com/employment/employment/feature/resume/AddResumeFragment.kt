package com.employment.employment.feature.resume

import android.app.DatePickerDialog
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.base.DateFragment
import com.employment.employment.common.firebase.data.QualificationModel
import com.employment.employment.common.firebase.data.generateYearsList
import com.employment.employment.common.firebase.data.listOfNationality
import com.employment.employment.common.firebase.data.listOfQualifications
import com.employment.employment.common.firebase.data.listOfResidencyTypes
import com.employment.employment.databinding.FragmentAddResumeBinding
import com.employment.employment.feature.resume.AddQualificationFragment.Companion.Qualification
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddResumeFragment : BaseFragment<FragmentAddResumeBinding>(), DatePickerDialog.OnDateSetListener {

    private var selectedDate: Date? = null

    private val qualificationsAdapter by lazy {
        QualificationsAdapter()
    }
    override fun initBinding() = FragmentAddResumeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            llBirthDate.setOnClickListener {
                DateFragment(this@AddResumeFragment).also {
                    it.dialog?.window?.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                }.show(parentFragmentManager, "date")
            }

            tvAddQualification.setOnClickListener {
                findNavController().navigate(AddResumeFragmentDirections.actionAddResumeFragmentToAddQualificationFragment())
            }
            rvQualifications.adapter = qualificationsAdapter
        }
        initSpinners()
        listen()
    }


    private fun listen() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<QualificationModel>(
            Qualification
        )
            ?.observe(
                viewLifecycleOwner
            ) { result ->

                qualificationsAdapter.list.add(result)
                qualificationsAdapter.notifyDataSetChanged()
            }
    }


    private fun initSpinners(){

        binding.apply {
            spinnerNationality.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                listOfNationality.toTypedArray()
            )

            spinnerResidence.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                listOfResidencyTypes.toTypedArray()
            )
        }
    }



    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar  = Calendar.getInstance()
        calendar[year, month] = dayOfMonth
        selectedDate = Date(calendar.timeInMillis)
        val date = SimpleDateFormat("dd/MM/yyyy").format(selectedDate)
        binding.tvDayPicker.text = date.split("/")[0]
        binding.tvMonthPicker.text = date.split("/")[1]
        binding.tvYearPicker.text = date.split("/")[2]
    }

}