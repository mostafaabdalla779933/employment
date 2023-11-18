package com.employment.employment.feature.resume

import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragmentDialog
import com.employment.employment.common.firebase.data.QualificationModel
import com.employment.employment.common.firebase.data.generateYearsList
import com.employment.employment.common.firebase.data.listOfNationality
import com.employment.employment.common.firebase.data.listOfQualifications
import com.employment.employment.common.getDouble
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentAddQualificationBinding


class AddQualificationFragment : BaseFragmentDialog<FragmentAddQualificationBinding>() {



    override fun initBinding() = FragmentAddQualificationBinding.inflate(layoutInflater)

    override fun onDialogCreated() {
        isCancelable = false

        binding.apply {
            ivClose.setOnClickListener {
                findNavController().popBackStack()
            }

            spinnerQualification.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                listOfQualifications.toTypedArray()
            )

            spinnerGraduationCountry.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                listOfNationality.toTypedArray()
            )

            spinnerGraduationYear.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                generateYearsList().toTypedArray()
            )

            btnAdd.setOnClickListener {
                validate()
            }

        }
    }

    private fun validate(){
        binding.apply {
            when{
                etGraduationUniversity.isStringEmpty() ->{
                    requireContext().showMessage("fill Graduation University")
                }

                etGraduationGrade.isStringEmpty() || etGraduationGrade.getDouble() == 0.0 || etGraduationGrade.getDouble() > 100->{
                    requireContext().showMessage("invalid Graduation Grade")
                }

                etCollegeName.isStringEmpty() ->{
                    requireContext().showMessage("fill College Name")
                }

                else ->{
                    findNavController().previousBackStackEntry?.savedStateHandle?.set(
                        Qualification,
                        QualificationModel(
                            qualification = spinnerQualification.selectedItem.toString(),
                            graduationCountry = spinnerGraduationCountry.selectedItem.toString(),
                            graduationUniversity = etGraduationUniversity.getString(),
                            graduationGrade = etGraduationGrade.getString(),
                            collegeName = etCollegeName.getString(),
                            graduationYear = spinnerGraduationYear.selectedItem.toString()
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

    companion object {
        const val Qualification = "Qualification"
    }


}