package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Event
import ru.netology.nework.dto.Media
import ru.netology.nework.dto.MediaUpload

interface EventRepository {

    val data: Flow<List<Event>>

    suspend fun getAllEvents()

    suspend fun createNewEvent(event: Event)

    suspend fun getEventById(id: Long)

    suspend fun editEvent(event: Event)

    suspend fun likeEventById(id: Long)

    suspend fun disLikeEventById(id: Long)

    suspend fun removeEventById(id: Long)

    suspend fun takePartEvent(id: Long)

    suspend fun unTakePartEvent(id: Long)

    suspend fun saveWithAttachment(event: Event, upload: MediaUpload)

    suspend fun upload(upload: MediaUpload): Media

}