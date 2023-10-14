package com.employment.employment.feature.auth


import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.databinding.FragmentSignUpUserBinding


class SignUpUserFragment : BaseFragment<FragmentSignUpUserBinding>(){
    override fun initBinding()= FragmentSignUpUserBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {

        setActions()
    }


    private fun setActions(){
        binding.apply {
            btnUserLogin.setOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

}