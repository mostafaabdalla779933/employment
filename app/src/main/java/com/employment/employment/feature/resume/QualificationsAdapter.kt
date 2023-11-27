package com.employment.employment.feature.resume

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.employment.employment.common.firebase.data.ExperienceModel
import com.employment.employment.common.firebase.data.QualificationModel
import com.employment.employment.common.toYearsAndMonths
import com.employment.employment.databinding.ItemEducationDataBinding
import com.employment.employment.databinding.ItemExperienceBinding
import com.employment.employment.databinding.ItemExperienceDataBinding
import com.employment.employment.databinding.ItemQualificationBinding


class QualificationsAdapter(var list: MutableList<QualificationModel?> = mutableListOf()) :
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
        list[position]?.let { holder.onBind(it, position) }
    }
}


class QualificationReadAdapter(var list: MutableList<QualificationModel?> = mutableListOf()) :
    RecyclerView.Adapter<QualificationReadAdapter.ViewHolder>() {

    inner class ViewHolder(private val rowView: ItemEducationDataBinding) :
        RecyclerView.ViewHolder(rowView.root) {
        fun onBind(item: QualificationModel, position: Int) {
            rowView.apply {
                tvQualification.text = item.qualification
                tvYear.text = "Graduation year: ${item.graduationYear}"
                tvUniversityName.text = "University: ${item.graduationUniversity}"
                tvCollegeName.text =  "College name: ${item.collegeName ?: ""}"
                tvGrade.text = if(item.GPA == true) "Grade: ${item.graduationGrade}"  else "Grade: ${item.graduationGrade}%"
                tvGraduationCountry.text = "Graduation Country: ${item.graduationCountry}"
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemEducationDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position]?.let { holder.onBind(it, position) }
    }
}


class ExperiencesAdapter(var list: MutableList<ExperienceModel?> = mutableListOf()) :
    RecyclerView.Adapter<ExperiencesAdapter.ViewHolder>() {

    inner class ViewHolder(private val rowView: ItemExperienceBinding) :
        RecyclerView.ViewHolder(rowView.root) {
        fun onBind(item: ExperienceModel, position: Int) {
            rowView.apply {
                tvJobTitle.text = item.jobTitle
                tvCompanyName.text = item.companyName
                tvAboutCompany.text = item.companyAbout
                tvEmail.text = item.companyEmail
                tvCompanyNumber.text = item.companyNumber
                tvMobile.text = item.mobile
                tvExperienceYears.text = item.experience?.toYearsAndMonths()
                tvCompanyWebsite.text = item.companyWebsite
                if(item.companyWebsite.isNullOrEmpty()){
                    website.visibility = View.GONE
                    tvCompanyWebsite.visibility = View.GONE
                }else{
                    website.visibility = View.VISIBLE
                    tvCompanyWebsite.visibility = View.VISIBLE
                }

                if(item.companyNumber.isNullOrEmpty()){
                    companyNumber.visibility = View.GONE
                    tvCompanyNumber.visibility = View.GONE
                }else{
                    companyNumber.visibility = View.VISIBLE
                    tvCompanyNumber.visibility = View.VISIBLE
                }
                ivClose.setOnClickListener {
                    list.removeAt(position)
                    notifyItemRemoved(position)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExperienceBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position]?.let { holder.onBind(it, position) }
    }
}


class ExperiencesReadAdapter(var list: MutableList<ExperienceModel?> = mutableListOf()) :
    RecyclerView.Adapter<ExperiencesReadAdapter.ViewHolder>() {

    inner class ViewHolder(private val rowView: ItemExperienceDataBinding) :
        RecyclerView.ViewHolder(rowView.root) {
        fun onBind(item: ExperienceModel, position: Int) {
            rowView.apply {
                tvJobTitle.text = item.jobTitle
                tvMobile.text = "Mobile: ${item.mobile}"
                tvCompanyName.text = "Company name: ${item.companyName}"
                tvAboutCompany.text = item.companyAbout
                tvCompanyNumber.text = "Company number: ${item.companyNumber}"
                tvExperience.text = "Experience: ${item.experience?.toYearsAndMonths() ?: ""}"
                tvGmail.text = item.companyEmail
                tvWebsite.text = item.companyWebsite

                if(item.companyWebsite.isNullOrEmpty()){
                    tvWebsite.visibility = View.GONE
                }else{
                    tvWebsite.visibility = View.VISIBLE
                }

                if(item.companyNumber.isNullOrEmpty()){
                    tvCompanyNumber.visibility = View.GONE
                }else{
                    tvCompanyNumber.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemExperienceDataBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        list[position]?.let { holder.onBind(it, position) }
    }
}