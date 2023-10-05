package com.employment.employment.common.base

import android.content.Context
import android.view.WindowManager
import com.employment.employment.common.base.BaseDialog
import com.employment.employment.databinding.DialogProgressLayoutBinding



class ProgressDialog(context: Context) : BaseDialog<DialogProgressLayoutBinding>(context) {

    override fun initBinding(): DialogProgressLayoutBinding {
        return DialogProgressLayoutBinding.inflate(layoutInflater)
    }

    override fun onDialogCreated() {
         setCancelable(false)
        window!!.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }
}