package com.employment.employment.feature.userDashboard


import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.databinding.FragmentUserResumeBinding


class UserResumeFragment : BaseFragment<FragmentUserResumeBinding>() {
    override fun initBinding() = FragmentUserResumeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {

            tvMyResume.setOnClickListener {
                if(FirebaseHelp.user?.resume == null){
                    showErrorMsg("fill your resume first")
                }else{
                    findNavController().navigate(UserResumeFragmentDirections.actionUserResumeFragmentToMyResumeFragment())
                }
            }

            tvAddResume.setOnClickListener {
                findNavController().navigate(UserResumeFragmentDirections.actionUserResumeFragmentToAddResumeFragment())
            }

            ivNotification.setOnClickListener {
                findNavController().navigate(UserResumeFragmentDirections.actionUserResumeFragmentToNotificationFragment())
            }
        }
    }
}