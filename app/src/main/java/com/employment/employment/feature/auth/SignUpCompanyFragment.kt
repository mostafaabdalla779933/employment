package com.employment.employment.feature.auth


import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.SelectedLocation
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.setImageFromUri
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentSignUpCompanyBinding


class SignUpCompanyFragment :BaseFragment<FragmentSignUpCompanyBinding>(){

    private var listOfUsers = mutableListOf<UserModel>()
    private var selectedUri : Uri?  =null

    private var selectedLocation:SelectedLocation?=null
    override fun initBinding()=FragmentSignUpCompanyBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setActions()
    }


    private fun setActions(){

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<SelectedLocation>("location")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                setAddress(result)
            }

        binding.apply {
            btnLogin.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSignUp.setOnClickListener {
               // validate()
            }

            tvLocation.setOnClickListener {
                findNavController().navigate(R.id.selectLocationFragment)
            }

            ivCompanyLogo.setOnClickListener {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    chooseUserPhotoFromGallery33()
                }else{
                    chooseUserPhotoFromGallery()
                }
            }
        }
    }

    private fun setAddress(location: SelectedLocation) {
        binding.apply {
            selectedLocation = location
            tvLocation.text = location.address
        }
    }

    @SuppressLint("InlinedApi")
    private fun chooseUserPhotoFromGallery33() {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {

                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                startForGallery.launch(intent)
            }
        } catch (e: Exception) {
            showErrorMsg("something went wrong")
        }
    }

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            var accept = false
            permissions.entries.forEach {
                accept = it.value
            }
            if (accept) {
                startForGallery.launch(
                    Intent(
                        Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                    )
                )
            } else {
                showErrorMsg("permission denied")
            }
        }

    private val startForGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.data?.let { uri ->
                    selectedUri = uri
                    binding.ivCompanyLogo.setImageFromUri(uri, requireContext())

                }
            }
        }


    private fun chooseUserPhotoFromGallery() {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED) {

                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    )
                )
            } else {
                val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                startForGallery.launch(intent)
            }
        } catch (e: Exception) {
            showErrorMsg("something went wrong")
        }
    }


//    private fun validate(){
//        binding.apply {
//            when{
//                etFullName.isStringEmpty() ->{
//                    showErrorMsg("fill name")
//                }
//
//                etPhoneNumber.isStringEmpty() ->{
//                    showErrorMsg("fill mobile number")
//                }
//
//                selectedDate == null ->{
//                    showErrorMsg("fill birthdate")
//                }
//
//                isValidEmail(etEmail.getString()).not() ->{
//                    showErrorMsg("invalid email")
//                }
//                listOfUsers.none { e -> e.email == etEmail.getString() }.not() ->{
//                    showErrorMsg("this email already in use")
//                }
//
//                etPass.isStringEmpty() ->{
//                    showErrorMsg("fill password")
//                }
//
//                etPass.getString() != etConfirmPass.getString() ->{
//                    showErrorMsg("invalid confirmation password")
//                }
//                else->{
//                    signWithFirebase()
//                }
//
//            }
//        }
//
//    }

//    private fun signWithFirebase(){
//        binding.apply {
//            val intent = Intent(requireContext(), AddUserService::class.java)
//
//            val user = UserModel(
//                email = etEmail.getString(),
//                password = etPass.getString(),
//                name = etFullName.getString(),
//                mobile = etPhoneNumber.getString(),
//                userType = UserType.User.value,
//                specialization = spinnerSpecialization.selectedItem.toString(),
//                birthdate = SimpleDateFormat("dd MM yyyy").format(selectedDate),
//                isMale = rbMale.isChecked
//            )
//            intent.putExtra(FirebaseHelp.USERS, user)
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                requireContext().startForegroundService(intent);
//            } else {
//                requireContext().startService(intent);
//            }
//            requireContext().showMessage("uploading your data")
//            findNavController().popBackStack()
//        }
//    }

    private fun getData(){
        showLoading()
        FirebaseHelp.getAllObjects<UserModel>(FirebaseHelp.USERS,{
            hideLoading()
            listOfUsers = it
        },{
            hideLoading()
            requireContext().showMessage(it)
            findNavController().popBackStack()
        })
    }

}