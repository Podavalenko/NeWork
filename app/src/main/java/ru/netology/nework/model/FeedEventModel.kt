package ru.netology.nework.model

import ru.netology.nework.dto.Event

class FeedEventModel(
    val events: List<Event> = emptyList(),
)