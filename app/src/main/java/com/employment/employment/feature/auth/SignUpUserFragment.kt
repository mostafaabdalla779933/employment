package com.employment.employment.feature.auth


import android.content.Intent
import android.os.Build
import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.common.isValidEmail
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentSignUpUserBinding


class SignUpUserFragment : BaseFragment<FragmentSignUpUserBinding>() {

    private var listOfUsers = mutableListOf<UserModel>()
    override fun initBinding() = FragmentSignUpUserBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setActions()
    }


    private fun setActions() {
        binding.apply {
            btnUserLogin.setOnClickListener {
                findNavController().popBackStack()
            }

            btnUserSignUp.setOnClickListener {
                validate()
            }
        }
        getData()
    }


    private fun validate() {
        binding.apply {
            when {
                etUserFirstName.isStringEmpty() -> {
                    showErrorMsg("fill first name")
                }

                etUserLastName.isStringEmpty() -> {
                    showErrorMsg("fill last name")
                }

                isValidEmail(etUserEmail.getString()).not() -> {
                    showErrorMsg("invalid email")
                }

                listOfUsers.none { e -> e.email == etUserEmail.getString() }.not() -> {
                    showErrorMsg("this email already in use")
                }

                etUserMobile.isStringEmpty() -> {
                    showErrorMsg("fill mobile")
                }

                etUserPassword.isStringEmpty() -> {
                    showErrorMsg("fill password")
                }

                etUserPassword.getString() != etUserConfirmPassword.getString() -> {
                    showErrorMsg("invalid confirmation password")
                }

                else -> {
                    signWithFirebase()
                }

            }
        }

    }

    private fun signWithFirebase() {
        binding.apply {
            val intent = Intent(requireContext(), AddUserService::class.java)

            val user = UserModel(
                email = etUserEmail.getString(),
                password = etUserPassword.getString(),
                firstName = etUserFirstName.getString(),
                lastName = etUserLastName.getString(),
                mobile = etUserMobile.getString(),
                userType = UserType.User.value
            )
            intent.putExtra(FirebaseHelp.USERS, user)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().startForegroundService(intent);
            } else {
                requireContext().startService(intent);
            }
            requireContext().showMessage("uploading your data")
            findNavController().popBackStack()
        }
    }

    private fun getData() {
        showLoading()
        FirebaseHelp.getAllObjects<UserModel>(FirebaseHelp.USERS, {
            hideLoading()
            listOfUsers = it
        }, {
            hideLoading()
            requireContext().showMessage(it)
            findNavController().popBackStack()
        })
    }

}