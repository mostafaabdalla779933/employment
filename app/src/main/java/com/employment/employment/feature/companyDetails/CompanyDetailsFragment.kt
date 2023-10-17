package com.employment.employment.feature.companyDetails

import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.databinding.FragmentCompanyDetailsBinding
import com.employment.employment.feature.CompanyDetailsFragmentArgs


class CompanyDetailsFragment : BaseFragment<FragmentCompanyDetailsBinding>() {

    val args: CompanyDetailsFragmentArgs by navArgs()
    override fun initBinding()= FragmentCompanyDetailsBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        displayData()
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun displayData(){
        binding.apply {
            Glide.with(requireContext()).load(args.company.profileUrl).into(ivCompany)
            tvCompanyName.text = args.company.name
            tvCompanyNumber.text = args.company.mobile
            tvAboutCompany.text = args.company.about
            tvCompanyAddress.text = args.company.address
            tvCompanyWebsite.text = args.company.companyWebsite
            tvCompanyEmail.text = args.company.email
            tvCompanyBranch.text = args.company.listOfBranches.firstOrNull()?.name ?: ""
        }
    }


}