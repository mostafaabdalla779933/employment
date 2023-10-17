package com.employment.employment.feature.userDashboard


import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.databinding.FragmentUserResumeBinding


class UserResumeFragment : BaseFragment<FragmentUserResumeBinding>() {
    override fun initBinding() = FragmentUserResumeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {

            tvMyResume.setOnClickListener {
                findNavController().navigate(UserResumeFragmentDirections.actionUserResumeFragmentToMyResumeFragment())
            }

            tvAddResume.setOnClickListener {

            }
        }
    }
}