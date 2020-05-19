package com.example.songme.utils

import android.content.Context
import android.view.View
import android.widget.Toast

fun Context.showMessage(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.OnClickListener.assignViews(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}
