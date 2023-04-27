package ru.netology.nework.repository

import kotlinx.coroutines.flow.Flow
import ru.netology.nework.dto.Post

interface WallRepository {

    val data: Flow<List<Post>>

    suspend fun getUserWall(id: Long)

    suspend fun likePostsOnWall(authorId: Long, postId: Long)

    suspend fun disLikePostsOnWall(authorId: Long, postId: Long)

}