package com.employment.employment.feature.resume

import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.databinding.FragmentMyResumeBinding

class MyResumeFragment : BaseFragment<FragmentMyResumeBinding>() {
    override fun initBinding() = FragmentMyResumeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}