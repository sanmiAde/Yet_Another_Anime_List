package com.sanmidev.yetanotheranimelist.utils.ui

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sanmidev.yetanotheranimelist.MainActivity
import com.sanmidev.yetanotheranimelist.R
import com.sanmidev.yetanotheranimelist.utils.EndlessRecyclerViewScrollListener

fun View.gone() {
    this.visibility = View.GONE
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun fireToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}


inline fun View.showIf(condition: (View) -> Boolean) {
    if (condition(this)) {
        visible()
    } else {
        gone()
    }
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


inline fun RecyclerView.initEndlessScrollListener(crossinline callback: () -> Unit) {

    when (val layoutManager = layoutManager) {
        is GridLayoutManager -> {
            this.addOnScrollListener(
                object : EndlessRecyclerViewScrollListener(layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        callback()
                    }

                })
        }

        is LinearLayoutManager -> {
            this.addOnScrollListener(
                object : EndlessRecyclerViewScrollListener(layoutManager) {
                    override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView) {
                        callback()
                    }

                })
        }

    }
}

