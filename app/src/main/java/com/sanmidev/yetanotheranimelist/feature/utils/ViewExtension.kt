package com.sanmidev.yetanotheranimelist.feature.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.R

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun fireToast(context: Context, message : String){
    Toast.makeText( context, message, Toast.LENGTH_SHORT).show()
}


/***
 * Iniialises the toolbar button with a black icon
 * @param activity is the [MainActivity]
 * @param toolbar is the toolbar to be initialised
 * @author oluwasanmi Aderibigbe
 */
fun initToolbarButton(activity: Activity, toolbar: Toolbar) {
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp)
    toolbar.setNavigationOnClickListener {
        (activity as MainActivity).onBackPressed()
    }
}
