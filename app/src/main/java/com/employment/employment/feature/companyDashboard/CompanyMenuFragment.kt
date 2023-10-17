package com.employment.employment.feature.companyDashboard


import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.databinding.FragmentCompanyMenuBinding
import com.employment.employment.feature.MainActivity


class CompanyMenuFragment : BaseFragment<FragmentCompanyMenuBinding>() {
    override fun initBinding() = FragmentCompanyMenuBinding.inflate(layoutInflater)

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

            tvCreateJobApp.setOnClickListener {
                findNavController().navigate(CompanyMenuFragmentDirections.actionCompanyMenuFragmentToCreateJobApplicationFragment())
            }


        }
    }


}