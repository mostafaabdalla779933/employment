package com.employment.employment.feature.companyDashboard

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.databinding.ItemEmployeeBinding

class EmployeesAdapter(val onClick:(UserModel)->Unit): ListAdapter<UserModel, EmployeesAdapter.ViewHolder>(
    UserDiffCallback()){

    inner class ViewHolder(private val rowView: ItemEmployeeBinding): RecyclerView.ViewHolder(rowView.root){
        fun onBind(user: UserModel, position: Int){
            rowView.apply {
                tvEmployeeName.text= user.getFullName()
                tvEmployeeNumber.text = user.mobile
               // tvJobTitle.text = user.
                root.setOnClickListener {
                    onClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemEmployeeBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position),position)
    }

    class UserDiffCallback : DiffUtil.ItemCallback<UserModel>() {
        override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem.userId == newItem.userId
        }

        override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
            return oldItem == newItem
        }
    }
}