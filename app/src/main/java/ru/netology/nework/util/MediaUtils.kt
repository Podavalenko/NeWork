package ru.netology.nework.util

import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import ru.netology.nework.R
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.Post
import ru.netology.nework.dto.User

object MediaUtils {

    fun loadPostImage(imageView: ImageView, url: String, post: Post) {
        Glide.with(imageView)
            .load(post.attachment?.url)
            .error(R.drawable.ic_error)
            .placeholder(R.drawable.ic_loading)
            .timeout(10_000)
            .into(imageView)
    }

    fun loadPostAvatar(imageView: ImageView, url: String, post: Post) {
        Glide.with(imageView)
            .load(post.authorAvatar)
            .error(R.drawable.ic_error)
            .placeholder(R.drawable.ic_loading)
            .circleCrop()
            .timeout(10_000)
            .into(imageView)
    }

    fun getRealPathFromUri(uri: Uri, requireActivity: FragmentActivity): String? {
        var cursor: Cursor? = null
        return try {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            cursor = requireActivity.contentResolver.query(uri, projection, null, null, null)
            val columnIndex = cursor?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor?.moveToFirst()
            cursor?.getString(columnIndex!!)
        } finally {
            cursor?.close()
        }
    }

    fun loadEventImage(imageView: ImageView, url: String, event: Event) {
        Glide.with(imageView)
            .load("$url/media/${event.attachment?.url}")
            .error(R.drawable.ic_error)
            .placeholder(R.drawable.ic_loading)
            .timeout(10_000)
            .into(imageView)
    }

    fun loadUserAvatar(imageView: ImageView, url: String, user: User) {
        Glide.with(imageView)
            .load("$url/avatars/${user.avatar}")
            .error(R.drawable.ic_error)
            .placeholder(R.drawable.ic_loading)
            .circleCrop()
            .timeout(10_000)
            .into(imageView)
    }

}