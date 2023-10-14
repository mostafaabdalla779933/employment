package com.employment.employment.feature.auth

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.databinding.FragmentSignInBinding


class SignInFragment :BaseFragment<FragmentSignInBinding>(){
    override fun initBinding()=FragmentSignInBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setActions()
    }

    private fun setActions(){
        binding.apply {

            btnSignUp.setOnClickListener {
                findNavController().navigate(SignInFragmentDirections.actionSignInFragmentToSignUpFragment())
            }
        }
    }




}