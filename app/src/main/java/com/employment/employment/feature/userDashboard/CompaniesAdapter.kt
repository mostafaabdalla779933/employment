package com.employment.employment.feature.userDashboard

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.employment.employment.common.firebase.data.UserModel
import com.employment.employment.databinding.ItemCompanyBinding



class CompaniesAdapter(val onClick:(UserModel)->Unit): ListAdapter<UserModel, CompaniesAdapter.ViewHolder>(
    UserDiffCallback()){

    inner class ViewHolder(private val rowView: ItemCompanyBinding): RecyclerView.ViewHolder(rowView.root){
        fun onBind(user: UserModel, position: Int){
            rowView.apply {

                tvCompanyName.text= user.name
                ivCompanyPhone.text = user.mobile
                Glide.with(itemView.context).load(user.profileUrl).into(ivCompanyLogo)
                root.setOnClickListener {
                    onClick(user)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemCompanyBinding.inflate(LayoutInflater.from(parent.context),parent,false))
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