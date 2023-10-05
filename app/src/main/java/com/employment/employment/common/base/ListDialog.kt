package com.employment.employment.common.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.employment.employment.databinding.ListDailogBinding
import com.employment.employment.databinding.StringItemBinding


class ListDialog(val list :List<String>,val title:String ,var onSelect:(String)->Unit ) : DialogFragment(){

    lateinit var binding : ListDailogBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = ListDailogBinding.inflate(layoutInflater)

        binding.tvTitle.text = title
        binding.ivCancle.setOnClickListener {
            this.dismiss()
        }
        binding.rv.adapter = StringAdapter(list = list){ str ->
            onSelect(str)
            this.dismiss()
        }
        return binding.root
    }
}

class StringAdapter(var list:List<String>, var onSelect: (String)->Unit): RecyclerView.Adapter<StringAdapter.ViewHolder>() {

    class ViewHolder(var rowView: StringItemBinding): RecyclerView.ViewHolder(rowView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding= StringItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(viewBinding)
    }

    override fun getItemCount(): Int =list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rowView.tvStr.text=list.get(position)
        holder.rowView.tvStr.setOnClickListener{
            onSelect(list.get(position))
        }
    }
}