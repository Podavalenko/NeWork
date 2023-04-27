package ru.netology.nework.viewmodel

import android.net.Uri
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.netology.nework.auth.AppAuth
import ru.netology.nework.dto.MediaUpload
import ru.netology.nework.dto.User
import ru.netology.nework.enumeration.ActionType
import ru.netology.nework.model.AvatarModel
import ru.netology.nework.model.FeedModelState
import ru.netology.nework.repository.UserRepository
import java.io.File
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    val repository: UserRepository,
    auth: AppAuth
) : ViewModel() {

    private val _dataState = MutableLiveData<FeedModelState>()
    val dataState: LiveData<FeedModelState>
        get() = _dataState

    var lastAction: ActionType? = null

    val data = repository.data.asLiveData(Dispatchers.Default)

    val user = MutableLiveData<User>()

    var lastId: Long? = null

    private val noAvatar = AvatarModel()

    private val _avatar = MutableLiveData(noAvatar)
    val avatar: LiveData<AvatarModel>
        get() = _avatar

    fun tryAgain() {
        when (lastAction) {
            ActionType.LOAD -> retryGetAllUsers()
            ActionType.GETBYID -> retryGetById()
            else -> retryGetAllUsers()
        }
    }

    private var profileId = auth.authStateFlow.value.id

    init {
        getUserById(profileId)
        getAllUsers()
    }

    fun getAllUsers() = viewModelScope.launch {
        lastAction = ActionType.LOAD
        try {
            _dataState.postValue(FeedModelState(loading = true))
            repository.getAllUsers()
            _dataState.value = FeedModelState()
        } catch (e: Exception) {
            _dataState.value = FeedModelState(error = true)
        }
        lastAction = null
    }

    fun retryGetAllUsers() {
        getAllUsers()
    }

    fun getUserById(id: Long) {
        viewModelScope.launch {
            lastAction = ActionType.GETBYID
            lastId = id
            try {
                _dataState.postValue(FeedModelState(loading = true))
                val gotUser = repository.getUserById(id)
                _dataState.value = FeedModelState()
                user.value = gotUser
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
            lastAction = null
            lastId = null
        }

    }

    fun retryGetById() {
        lastId?.let {
            getUserById(it)
        }
    }

    fun saveUserAvatar(user: User, uri: Uri?, file: File?) {
        viewModelScope.launch {
            try {
                when (_avatar.value) {
                    noAvatar -> return@launch
                    else -> {
                        _avatar.value?.file?.let { file ->
                            repository.saveUserAvatar(
                                user,
                                MediaUpload(file),
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _dataState.value = FeedModelState(error = true)
            }
            _avatar.value = noAvatar
        }

    }

}

