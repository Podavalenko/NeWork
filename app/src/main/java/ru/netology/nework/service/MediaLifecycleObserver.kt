package ru.netology.nework.service

import android.media.MediaPlayer
import androidx.lifecycle.LifecycleObserver


class MediaLifecycleObserver: LifecycleObserver {

    var player: MediaPlayer? = MediaPlayer()

    fun play() {
        player?.setOnPreparedListener {
            it.start()
        }
        player?.prepareAsync()
    }

}