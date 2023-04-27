package ru.netology.nework.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Coordinates(
    val lat: Double,
    val long: Double,
) : Parcelable