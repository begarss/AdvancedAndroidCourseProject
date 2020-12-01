package com.example.djangotestapp.utils

import android.app.Activity
import android.app.AlertDialog
import com.example.djangotestapp.R
import com.example.djangotestapp.ui.MainActivity

class LoadingDialog(private val mainActivity: Activity) {
    private lateinit var dialog: AlertDialog
    fun startLoading() {
        val builder = AlertDialog.Builder(mainActivity)
        val inflater = mainActivity.layoutInflater
        builder.setView(inflater.inflate(R.layout.custom_dialog, null))
        builder.setCancelable(true)
        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        dialog.dismiss()
    }
}