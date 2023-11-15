package com.employment.employment.feature.resume

import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.databinding.FragmentAddResumeBinding

class AddResumeFragment : BaseFragment<FragmentAddResumeBinding>() {
    override fun initBinding() = FragmentAddResumeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}