package com.employment.employment.feature

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragmentDialog
import com.employment.employment.databinding.FragmentAddQualificationBinding
import com.employment.employment.databinding.FragmentSendInterviewDetailsBinding

class SendInterviewDetailsFragment : BaseFragmentDialog<FragmentSendInterviewDetailsBinding>() {

    override fun initBinding() = FragmentSendInterviewDetailsBinding.inflate(layoutInflater)

    override fun onDialogCreated() {
        isCancelable = false
    }

    override fun getTheme(): Int {
        super.getTheme()
        return R.style.DialogStyle
    }

}