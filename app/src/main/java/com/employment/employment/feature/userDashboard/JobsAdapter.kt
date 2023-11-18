package com.employment.employment.feature.userDashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.employment.employment.common.firebase.data.JobModel
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.databinding.ItemCompanyBinding
import com.employment.employment.databinding.ItemRecommendedJobBinding


class JobsAdapter(val onClick: (JobModel) -> Unit) : ListAdapter<JobModel, JobsAdapter.ViewHolder>(
    UserDiffCallback()
) {

    inner class ViewHolder(private val rowView: ItemRecommendedJobBinding) :
        RecyclerView.ViewHolder(rowView.root) {
        fun onBind(model: JobModel, position: Int) {
            rowView.apply {

                tvJobName.text = model.name
                tvTime.text = model.jobType
                tvCompanyName.text = model.company?.name
                Glide.with(itemView.context).load(model.company?.profileUrl).into(ivCompanyLogo)

                root.setOnClickListener {
                    onClick(model)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemRecommendedJobBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), position)
    }

    class UserDiffCallback : DiffUtil.ItemCallback<JobModel>() {
        override fun areItemsTheSame(oldItem: JobModel, newItem: JobModel): Boolean {
            return oldItem.hashed == newItem.hashed
        }

        override fun areContentsTheSame(oldItem: JobModel, newItem: JobModel): Boolean {
            return oldItem == newItem
        }
    }
}