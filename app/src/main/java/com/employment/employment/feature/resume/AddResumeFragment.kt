package com.employment.employment.feature.resume

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.view.Window
import android.widget.ArrayAdapter
import android.widget.DatePicker
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.employment.employment.R
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.base.DateFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.ExperienceModel
import com.employment.employment.common.firebase.data.QualificationModel
import com.employment.employment.common.firebase.data.ResumeModel
import com.employment.employment.common.firebase.data.listOfNationality
import com.employment.employment.common.firebase.data.listOfResidencyTypes
import com.employment.employment.common.getString
import com.employment.employment.common.isStringEmpty
import com.employment.employment.common.isValidEmail
import com.employment.employment.common.setImageFromUri
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentAddResumeBinding
import com.employment.employment.feature.resume.AddExperienceFragment.Companion.Experience
import com.employment.employment.feature.resume.AddQualificationFragment.Companion.Qualification
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class AddResumeFragment : BaseFragment<FragmentAddResumeBinding>(),
    DatePickerDialog.OnDateSetListener {

    private var selectedDate: Date? = null

    private var selectedUri: Uri? = null
    private val qualificationsAdapter by lazy {
        QualificationsAdapter()
    }

    private val experiencesAdapter by lazy {
        ExperiencesAdapter()
    }

    override fun initBinding() = FragmentAddResumeBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }

            ivEmployee.setOnClickListener {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    chooseUserPhotoFromGallery33()
                } else {
                    chooseUserPhotoFromGallery()
                }
            }

            llBirthDate.setOnClickListener {
                DateFragment(this@AddResumeFragment).also {
                    it.dialog?.window?.requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
                }.show(parentFragmentManager, "date")
            }

            tvAddQualification.setOnClickListener {
                findNavController().navigate(AddResumeFragmentDirections.actionAddResumeFragmentToAddQualificationFragment())
            }

            tvAddExperience.setOnClickListener {
                findNavController().navigate(AddResumeFragmentDirections.actionAddResumeFragmentToAddExperienceFragment())
            }

            btnConfirm.setOnClickListener {
                validate()
            }
            rvQualifications.adapter = qualificationsAdapter
            rvExperiences.adapter = experiencesAdapter
            selectedUri?.let {
                ivEmployee.setImageFromUri(it, requireContext())
            }
        }
        initSpinners()
        listen()
    }


    private fun listen() {

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<QualificationModel>(
            Qualification
        )
            ?.observe(
                viewLifecycleOwner
            ) { result ->

                qualificationsAdapter.list.add(result)
                qualificationsAdapter.notifyDataSetChanged()
            }


        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<ExperienceModel>(
            Experience
        )
            ?.observe(
                viewLifecycleOwner
            ) { result ->

                experiencesAdapter.list.add(result)
                experiencesAdapter.notifyDataSetChanged()
            }
    }


    private fun initSpinners() {

        binding.apply {
            spinnerNationality.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                listOfNationality.toTypedArray()
            )

            spinnerResidence.adapter = ArrayAdapter(
                requireContext(),
                R.layout.spinner_item,
                listOfResidencyTypes.toTypedArray()
            )
        }
    }


    private fun validate() {
        binding.apply {
            when {
                selectedUri == null -> {
                    showErrorMsg("select photo")
                }

                etFirstName.isStringEmpty() -> {
                    showErrorMsg("fill first name")
                }

                etLastName.isStringEmpty() -> {
                    showErrorMsg("fill last name")
                }

                selectedDate == null -> {
                    showErrorMsg("fill birth date")
                }

                etMobileNumber.isStringEmpty() -> {
                    showErrorMsg("fill mobile number")
                }

                etAddress.isStringEmpty() -> {
                    showErrorMsg("fill address")
                }

                isValidEmail(etEmail.getString()).not() -> {
                    showErrorMsg("invalid email")
                }

                qualificationsAdapter.list.isEmpty() -> {
                    showErrorMsg("add qualification")
                }

                experiencesAdapter.list.isEmpty() -> {
                    showErrorMsg("add experience")
                }

                else -> {
                    signWithFirebase()
                }

            }
        }

    }


    private fun signWithFirebase() {
        binding.apply {
            val intent = Intent(requireContext(), AddResumeService::class.java)

            val resume = ResumeModel(
                uri = selectedUri,
                firstName = etFirstName.getString(),
                lastName = etLastName.getString(),
                birthDate = selectedDate?.toString(),
                mobile = etMobileNumber.getString(),
                nationality = spinnerNationality.selectedItem.toString(),
                residenceType = spinnerResidence.selectedItem.toString(),
                address = etAddress.getString(),
                hasDriverLicense =
                when (rgLicense.checkedRadioButtonId) {
                    rbYes.id -> { true }
                    else -> { false }
                },
                male = when (rgGender.checkedRadioButtonId) {
                    rbMale.id -> { true }
                    else -> { false }
                },
                email = etEmail.getString(),
                listOfQualifications = qualificationsAdapter.list.toMutableList(),
                listOfExperiences = experiencesAdapter.list.toMutableList()
            )
            intent.putExtra(FirebaseHelp.RESUME, resume)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                requireContext().startForegroundService(intent);
            } else {
                requireContext().startService(intent);
            }
            requireContext().showMessage("uploading your data")
            findNavController().popBackStack()
        }
    }

    @SuppressLint("InlinedApi")
    private fun chooseUserPhotoFromGallery33() {
        try {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_MEDIA_IMAGES
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES
                    )
                )
            } else {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

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
                    binding.ivEmployee.setImageFromUri(uri, requireContext())

                }
            }
        }


    private fun chooseUserPhotoFromGallery() {
        try {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                requestMultiplePermissions.launch(
                    arrayOf(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    )
                )
            } else {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)

                startForGallery.launch(intent)
            }
        } catch (e: Exception) {
            showErrorMsg("something went wrong")
        }
    }


    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val calendar = Calendar.getInstance()
        calendar[year, month] = dayOfMonth
        selectedDate = Date(calendar.timeInMillis)
        val date = SimpleDateFormat("dd/MM/yyyy").format(selectedDate)
        binding.tvDayPicker.text = date.split("/")[0]
        binding.tvMonthPicker.text = date.split("/")[1]
        binding.tvYearPicker.text = date.split("/")[2]
    }

}