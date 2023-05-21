package ru.netology.nework.service

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent

class MediaLifecycleObserver: LifecycleObserver {

    var player: MediaPlayer? = MediaPlayer()

    fun play() {
        player?.setOnPreparedListener {
            it.start()
        }
        player?.prepareAsync()
    }

}