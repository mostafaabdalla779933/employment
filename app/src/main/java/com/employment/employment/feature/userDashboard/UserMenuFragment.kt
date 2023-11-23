package com.employment.employment.feature.userDashboard

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.databinding.FragmentUserMenuBinding
import com.employment.employment.feature.MainActivity
import com.employment.employment.feature.companyDashboard.CompanyMenuFragmentDirections


class UserMenuFragment : BaseFragment<FragmentUserMenuBinding>() {
    override fun initBinding() = FragmentUserMenuBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setActions()
    }


    private fun setActions() {
        binding.apply {
            btnLogout.setOnClickListener {
                FirebaseHelp.logout()
                startActivity(Intent(requireContext(), MainActivity::class.java))
                requireActivity().finish()
            }

            tvFullName.text = FirebaseHelp.user?.getFullName() ?: ""
            tvEmail.text = FirebaseHelp.user?.email ?: ""

            ivNotification.setOnClickListener {
                findNavController().navigate(UserHomeFragmentDirections.actionUserHomeFragmentToNotificationFragment())
            }
        }
    }

}