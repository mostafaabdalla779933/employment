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
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.BranchModel
import com.employment.employment.common.firebase.data.SelectedLocation
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.common.isValidEmail
import com.employment.employment.common.setImageFromUri
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentSignUpCompanyBinding


class SignUpCompanyFragment :BaseFragment<FragmentSignUpCompanyBinding>(){

    private var listOfUsers = mutableListOf<UserModel>()
    private var selectedUri : Uri?  =null
    private lateinit var branchAdapter:BranchAdapter
    private var selectedLocation:SelectedLocation?=null
    override fun initBinding()=FragmentSignUpCompanyBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        setActions()
        getData()

    }


    private fun setActions(){

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<SelectedLocation>("location")
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                setAddress(result)
            }

        binding.apply {
            if(!::branchAdapter.isInitialized){
                branchAdapter = BranchAdapter()
            }

            rvBranches.adapter = branchAdapter
            btnLogin.setOnClickListener {
                findNavController().popBackStack()
            }
            btnSignUp.setOnClickListener {
                validate()
            }

            tvLocation.setOnClickListener {
                findNavController().navigate(R.id.selectLocationFragment)
            }

            btnAddBranch.setOnClickListener {
                when{
                    etBranchName.isStringEmpty() ->{
                        showErrorMsg("fill branch name")
                    }
                    etBranchMobile.isStringEmpty() ->{
                        showErrorMsg("fill branch mobile")
                    }
                    else ->{
                        branchAdapter.list.add(BranchModel(name = etBranchName.getString(), mobile = etBranchMobile.getString()))
                        branchAdapter.notifyDataSetChanged()
                        etBranchName.setText("")
                        etBranchMobile.setText("")
                    }
                }
            }

            ivCompanyLogo.setOnClickListener {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
                    chooseUserPhotoFromGallery33()
                }else{
                    chooseUserPhotoFromGallery()
                }
            }
        }

        selectedUri?.let {
            binding.ivCompanyLogo.setImageFromUri(it, requireContext())
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


    private fun validate(){
        binding.apply {
            when{
                selectedUri == null -> {
                    showErrorMsg("select photo")
                }
                etCompanyName.isStringEmpty() ->{
                    showErrorMsg("fill name")
                }

                etMobileNumber.isStringEmpty() ->{
                    showErrorMsg("fill mobile number")
                }

                branchAdapter.list.isEmpty() ->{
                    showErrorMsg("add branch")
                }

                etAddress.isStringEmpty() ->{
                    showErrorMsg("fill address")
                }

                etAbout.isStringEmpty() ->{
                    showErrorMsg("fill about company")
                }

                selectedLocation == null ->{
                    showErrorMsg("select location")
                }

                isValidEmail(etEmail.getString()).not() ->{
                    showErrorMsg("invalid email")
                }

                listOfUsers.none { e -> e.email == etEmail.getString() }.not() ->{
                    showErrorMsg("this email already in use")
                }

                etPassword.isStringEmpty() ->{
                    showErrorMsg("fill password")
                }

                etPassword.getString() != etConfirmPassword.getString() ->{
                    showErrorMsg("invalid confirmation password")
                }
                else->{
                    signWithFirebase()
                }

            }
        }

    }

    private fun signWithFirebase(){
        binding.apply {
            val intent = Intent(requireContext(), AddUserService::class.java)

            val user = UserModel(
                email = etEmail.getString(),
                password = etPassword.getString(),
                name = etCompanyName.getString(),
                mobile = etMobileNumber.getString(),
                userType = UserType.Company.value,
                uri = selectedUri,
                listOfBranches = branchAdapter.list,
                address = etAddress.getString(),
                companyWebsite = etWebsite.getString(),
                about = etAbout.getString(),
                location = selectedLocation
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