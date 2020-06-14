package com.sanmidev.yetanotheranimelist.feature.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import com.sanmidev.yetanotheranimelist.MainActivity

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun fireToast(context: Context, message : String){
    Toast.makeText( context, message, Toast.LENGTH_SHORT).show()
}