package com.example.songme.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.songme.R

fun Context.showMessage(msg: String) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}

fun View.OnClickListener.assignViews(vararg views: View) {
    views.forEach { it.setOnClickListener(this) }
}

fun ImageView.loadImage(imageUrl: String, errorId: Int) {
    Glide.with(context)
        .load(imageUrl)
        .error(errorId)
        .circleCrop()
        .into(this)
}
