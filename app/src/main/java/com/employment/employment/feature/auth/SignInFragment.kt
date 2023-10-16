package com.employment.employment.feature.auth

import android.content.Intent
import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.databinding.FragmentSignInBinding
import com.employment.employment.feature.companyDashboard.CompanyDashboardActivity
import com.employment.employment.feature.userDashboard.UserDashboardActivity


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
            btnLogin.setOnClickListener {
                validate()
            }
        }
    }

    private fun validate(){
        if (binding.etUsername.isStringEmpty()){
            showErrorMsg("fill Username")
        }else if(binding.etPassword.isStringEmpty()){
            showErrorMsg("fill password")
        }else{
            signIn()
        }
    }

    private fun signIn(){
        showLoading()
        FirebaseHelp.auth
            .signInWithEmailAndPassword(binding.etUsername.getString(), binding.etPassword.getString())
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    hideLoading()
                    check()
                } else {
                    hideLoading()
                    showErrorMsg(task.exception?.localizedMessage ?:  "something wrong")
                }
            }
    }

    private fun check() {
        FirebaseHelp.getUser({
            hideLoading()
            FirebaseHelp.user = it
            when(it.userType){
                UserType.User.value ->{
                    startActivity(Intent(requireContext(),UserDashboardActivity::class.java))
                    requireActivity().finish()
                }
                UserType.Company.value ->{
                    startActivity(Intent(requireContext(),CompanyDashboardActivity::class.java))
                    requireActivity().finish()
                }
            }
        }, {
            hideLoading()
            showErrorMsg(it)
        })
    }




}