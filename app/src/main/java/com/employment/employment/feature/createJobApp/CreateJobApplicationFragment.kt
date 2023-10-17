package com.employment.employment.feature.createJobApp

import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.databinding.FragmentCreateJobApplicationBinding


class CreateJobApplicationFragment : BaseFragment<FragmentCreateJobApplicationBinding>() {
    override fun initBinding()= FragmentCreateJobApplicationBinding.inflate(layoutInflater)
    override fun onFragmentCreated() {
        setActions()
    }

    private fun setActions(){

        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }


}