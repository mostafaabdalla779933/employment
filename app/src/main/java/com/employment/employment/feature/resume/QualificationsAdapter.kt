package com.employment.employment.feature.resume

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.employment.employment.common.firebase.data.QualificationModel
import com.employment.employment.databinding.ItemQualificationBinding


class QualificationsAdapter(var list: MutableList<QualificationModel> = mutableListOf()) :
    RecyclerView.Adapter<QualificationsAdapter.ViewHolder>() {

    inner class ViewHolder(private val rowView: ItemQualificationBinding) :
        RecyclerView.ViewHolder(rowView.root) {
        fun onBind(item: QualificationModel, position: Int) {
            rowView.apply {
                tvQualification.text = item.qualification
                tvCollegeName.text = item.collegeName
                tvGraduationCountry.text = item.graduationCountry
                tvGraduationGrade.text = item.graduationGrade
                tvGraduationYear.text = item.graduationYear
                tvGraduationUniversity.text = item.graduationUniversity
                ivClose.setOnClickListener {
                    list.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemQualificationBinding.inflate(
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