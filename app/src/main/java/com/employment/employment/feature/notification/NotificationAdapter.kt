package com.employment.employment.feature.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.employment.employment.common.firebase.data.NotificationModel
import com.employment.employment.databinding.ItemNotificationBinding

class NotificationAdapter(val onItemClicked: (NotificationModel) -> Unit) :
    ListAdapter<NotificationModel, NotificationAdapter.ViewHolder>(
        UserDiffCallback()
    ) {

    inner class ViewHolder(private val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(item: NotificationModel) {
            binding.apply {
                tvDesc.text = item.message
                tvDate.text = item.date

                root.setOnClickListener{
                    onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class UserDiffCallback : DiffUtil.ItemCallback<NotificationModel>() {
        override fun areItemsTheSame(
            oldItem: NotificationModel,
            newItem: NotificationModel
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: NotificationModel,
            newItem: NotificationModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
