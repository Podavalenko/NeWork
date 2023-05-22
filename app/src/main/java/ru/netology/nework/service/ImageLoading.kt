package ru.netology.nework.service

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation


fun ImageView.imageLoad(url: String, vararg transforms: BitmapTransformation = emptyArray()) =
    Glide.with(this)
        .load(url)
        .timeout(10_000)
        .transform(*transforms)
        .into(this)

