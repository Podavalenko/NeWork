package ru.netology.nework.dto

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import ru.netology.nework.enumeration.AttachmentType

@Parcelize
data class Attachment(
    val url: String,
    val type: AttachmentType,
)  : Parcelable