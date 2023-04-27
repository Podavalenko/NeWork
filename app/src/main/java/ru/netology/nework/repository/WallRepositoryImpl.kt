package ru.netology.nework.repository

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import ru.netology.nework.api.ApiService
import ru.netology.nework.dto.Post
import ru.netology.nework.error.ApiException
import ru.netology.nework.error.NetWorkException
import ru.netology.nework.error.UnknownException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WallRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : WallRepository {

    val wallList = mutableListOf<Post>()

    private val _wall = MutableLiveData<List<Post>>()

    override val data = _wall.asFlow().flowOn(Dispatchers.Default)

    override suspend fun getUserWall(id: Long) {
        try {
            val response = apiService.getWall(id)
            val posts = response.body() ?: throw ApiException(response.code(), response.message())
            for (post in posts) {
                wallList.add(post)
            }
            _wall.value = wallList
        } catch (e: IOException) {
            throw NetWorkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun likePostsOnWall(authorId: Long, postId: Long) {
        try {
            val response = apiService.likePostOnWall(authorId, postId)
            val post = response.body() ?: throw ApiException(response.code(), response.message())
            wallList.add(post)
            _wall.value = wallList
        } catch (e: IOException) {
            throw NetWorkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

    override suspend fun disLikePostsOnWall(authorId: Long, postId: Long) {
        try {
            val response = apiService.likePostOnWall(authorId, postId)
            val post = response.body() ?: throw ApiException(response.code(), response.message())
            wallList.add(post)
            _wall.value = wallList
        } catch (e: IOException) {
            throw NetWorkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }

}