package com.mukesh.assignment_android.utils

import android.content.Context
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.databinding.BindingAdapter

import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mukesh.assignment_android.R

fun getProgressDrawable(mContext: Context): CircularProgressDrawable {
    return CircularProgressDrawable(mContext).apply {
        strokeWidth = 10f
        centerRadius = 50f
        start()
    }
}

@BindingAdapter("android:imageUrl")
fun loadImage(view: ImageView, url: String?) {
    val options = RequestOptions().placeholder(getProgressDrawable(view.context.applicationContext))
        .error(R.mipmap.ic_launcher_round)
    Glide.with(view.context)
        .applyDefaultRequestOptions(options)
        .load(url)
        .into(view)
}

@BindingAdapter("android:bgColor")
fun setBackgroundColor(layout: LinearLayout, color: Int) {
    layout.setBackgroundColor(color)
}
