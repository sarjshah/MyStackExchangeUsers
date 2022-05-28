package com.practice.mystackexchangeusers.common

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.practice.mystackexchangeusers.R


fun ImageView.loadImage(context: Context, url: String) =
    Glide.with(context).applyDefaultRequestOptions(
        RequestOptions().placeholder(R.drawable.ic_launcher_foreground)
            .error(R.drawable.ic_launcher_background)
    ).load(url).into(this)