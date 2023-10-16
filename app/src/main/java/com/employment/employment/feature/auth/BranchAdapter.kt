package com.employment.employment.feature.auth

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.employment.employment.common.firebase.data.BranchModel
import com.employment.employment.databinding.ItemBranchBinding

class BranchAdapter(var list: MutableList<BranchModel> = mutableListOf()) :
    RecyclerView.Adapter<BranchAdapter.ViewHolder>() {

    inner class ViewHolder(private val rowView: ItemBranchBinding) :
        RecyclerView.ViewHolder(rowView.root) {
        fun onBind(item: BranchModel, position: Int) {
            rowView.apply {
                tvBranchName.text = item.name
                tvMobile.text = item.mobile
                ivDelete.setOnClickListener {
                    list.removeAt(position)
                    notifyDataSetChanged()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBranchBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position].let { holder.onBind(it, position) }
    }
}