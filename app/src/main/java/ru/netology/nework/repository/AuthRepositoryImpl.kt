package ru.netology.nework.repository

import ru.netology.nework.api.ApiService
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.error.ApiException
import ru.netology.nework.error.NetWorkException
import ru.netology.nework.error.UnknownException
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : AuthRepository {

    @Inject
    lateinit var auth: AppAuth

    override suspend fun signIn(login: String, pass: String) {
        try {
            val response = apiService.updateUser(login, pass)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val authState =
                response.body() ?: throw ApiException(response.code(), response.message())
            authState.token?.let { auth.setAuth(authState.id, it) }

        } catch (e: IOException) {
            throw NetWorkException
        } catch (e: Exception) {
            throw UnknownException
        }
        return
    }

    override suspend fun signUp(name: String, login: String, pass: String) {
        try {
            val response = apiService.registerUser(name, login, pass)
            if (!response.isSuccessful) {
                throw ApiException(response.code(), response.message())
            }
            val authState =
                response.body() ?: throw ApiException(response.code(), response.message())
            authState.token?.let { auth.setAuth(authState.id, it) }

        } catch (e: IOException) {
            throw NetWorkException
        } catch (e: Exception) {
            throw UnknownException
        }
    }
}