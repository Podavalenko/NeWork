package ru.netology.nework.model

import android.net.Uri
import ru.netology.nework.enumeration.AttachmentType
import java.io.File

class AttachmentModel(
    val uri: Uri? = null,
    val file: File? = null,
    val type: AttachmentType? = null
)