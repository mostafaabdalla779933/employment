package com.employment.employment.feature

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.getDayMonthAndYear
import com.employment.employment.databinding.FragmentJobRequestBinding


class JobRequestFragment : BaseFragment<FragmentJobRequestBinding>() {

    private val args: JobRequestFragmentArgs by navArgs()

    override fun initBinding() = FragmentJobRequestBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }

        displayData()
    }

    private fun displayData() {
        binding.apply {
            Glide.with(requireContext()).load(args.notification.from?.profileUrl).into(ivCompany)
            tvCompanyName.text = args.notification.from?.name
            tvCompanyNumber.text = args.notification.from?.mobile
            tvInterviewNumber.text = args.notification.from?.mobile
            tvInterviewAddress.text = args.notification.from?.location?.address ?: ""
            tvInterviewTime.text =  "${args.notification.interviewTime} ${args.notification.interviewTDate?.getDayMonthAndYear()}"

            tvInterviewAddress.paintFlags = tvInterviewAddress.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG

            tvInterviewAddress.setOnClickListener {
                val uri = "http://maps.google.com/maps?q=loc:${args.notification.from?.location?.lat?.toFloat()},${args.notification.from?.location?.long?.toFloat()}"
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                intent.setPackage("com.google.android.apps.maps")
                startActivity(intent)
            }
        }
    }

}