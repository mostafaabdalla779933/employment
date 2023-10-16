package com.employment.employment.feature.splash


import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.databinding.FragmentSplashBinding
import com.employment.employment.feature.companyDashboard.CompanyDashboardActivity
import com.employment.employment.feature.userDashboard.UserDashboardActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class SplashFragment : BaseFragment<FragmentSplashBinding>(){
    override fun initBinding()= FragmentSplashBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        checkNotificationPermission()
    }


    private fun navigate() {

        if (FirebaseHelp.getUserID().isNotEmpty()) {
            showLoading()
            FirebaseHelp.getUser({
                hideLoading()
                FirebaseHelp.user = it
                when(it.userType){
                    UserType.Company.value ->{
                        startActivity(Intent(requireContext(), CompanyDashboardActivity::class.java))
                        requireActivity().finish()
                    }
                    UserType.User.value ->{
                        startActivity(Intent(requireContext(), UserDashboardActivity::class.java))
                        requireActivity().finish()
                    }
                }
            }, {
                hideLoading()
                showErrorMsg(it)
            })
        }else{
            lifecycleScope.launch {
                delay(500)
                findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToSignInFragment())
            }
        }
    }

    private fun checkNotificationPermission() {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.POST_NOTIFICATIONS
                    )
                )
            } else {
                navigate()
            }
        } catch (e: Exception) {
            navigate()
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            navigate()

        }

}