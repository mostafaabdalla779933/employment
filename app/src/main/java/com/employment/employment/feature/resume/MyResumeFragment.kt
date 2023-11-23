package com.employment.employment.feature.resume

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.getDayMonthAndYear
import com.employment.employment.databinding.FragmentMyResumeBinding

class MyResumeFragment : BaseFragment<FragmentMyResumeBinding>() {

    val args : MyResumeFragmentArgs by navArgs()
    override fun initBinding() = FragmentMyResumeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

        }

        if(args.user != null){
            displayResume(args.user)
        }else {
            displayResume(FirebaseHelp.user)
        }


    }

    private fun displayResume(user:UserModel?) {
        binding.apply {
            user?.let {
                Glide.with(requireContext())
                    .load(it.resume?.profileUrl)
                    .placeholder(R.drawable.ic_employee)
                    .into(ivEmployee)
                tvDateOfBirth.text = it.resume?.birthDate?.getDayMonthAndYear()
                tvMobileNumber.text = it.mobile
                tvEmployeeNationality.text = it.resume?.nationality
                tvEmployeeResidence.text = it.resume?.residenceType
                tvEmployeeAddress.text = it.resume?.address
                rbLicense.isChecked = it.resume?.hasDriverLicense ?: false
                tvEmployeeGender.text = if (it.resume?.male == true) "Male" else "Female"
                tvEmployeeEmail.text = it.email
                it.resume?.listOfQualifications?.let { list ->
                    val qualificationsAdapter = QualificationReadAdapter(list)
                    rvEducationalData.adapter = qualificationsAdapter
                }
                it.resume?.listOfExperiences?.let { list ->
                    val experiencesAdapter = ExperiencesReadAdapter(list)
                    rvExperienceData.adapter = experiencesAdapter
                }
            }
        }
    }

}