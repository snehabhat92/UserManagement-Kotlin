package com.example.myapplication.util

import android.app.Activity
import android.view.View
import android.view.inputmethod.InputMethodManager

class Util {
    companion object {
        fun hideKeyboard(activity: Activity?) {
            if (activity == null) return
            val imm =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            var view = activity.currentFocus
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}