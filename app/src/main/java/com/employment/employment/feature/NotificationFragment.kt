package com.employment.employment.feature

import androidx.navigation.fragment.findNavController
import com.employment.employment.common.base.BaseFragment
import com.employment.employment.common.firebase.FirebaseHelp
import com.employment.employment.common.firebase.data.NotificationModel
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.common.firebase.data.UserType
import com.employment.employment.common.showMessage
import com.employment.employment.databinding.FragmentNotificationBinding
import com.employment.employment.feature.notification.NotificationAdapter

class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {

    private val adapter: NotificationAdapter by lazy {
        NotificationAdapter {
            it.type?.let { type ->
                if (type == UserType.User.value) {
                    findNavController()
                        .navigate(
                            NotificationFragmentDirections
                                .actionNotificationFragment2ToEmployeeDetailsFragment(
                                    it.from ?: UserModel()
                                )
                        )
                } else {
                    findNavController()
                        .navigate(
                            NotificationFragmentDirections
                                .actionNotificationFragment2ToJobRequestFragment(
                                    it ?: NotificationModel()
                                )
                        )
                }
            }
        }
    }

    override fun initBinding() = FragmentNotificationBinding.inflate(layoutInflater)

    override fun onFragmentCreated() {
        binding.apply {
            ivBack.setOnClickListener {
                findNavController().popBackStack()
            }
        }
        getData()
    }

    private fun getData() {
        showLoading()
        binding.rvNotifications.adapter = adapter
        FirebaseHelp.getAllObjects<NotificationModel>(FirebaseHelp.NOTIFICATION, {
            hideLoading()
            adapter.submitList(it.filter { notification -> notification.toUserId == FirebaseHelp.getUserID() })
        }, {
            hideLoading()
            requireContext().showMessage(it)
        })
    }

}