package com.brian.android_mvvm_architecture.ui.photo_detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.android_mvvm_architecture.data.repository.PhotoRepository
import com.brian.android_mvvm_architecture.di.Dispatcher
import com.brian.android_mvvm_architecture.di.PhotoDispatcher
import com.brian.android_mvvm_architecture.ui.mapper.toPhotoUi
import com.brian.android_mvvm_architecture.ui.photos.PhotoUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotoDetailViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    @Dispatcher(PhotoDispatcher.IO) private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val _uiState = MutableStateFlow<PhotoDetailUiState>(PhotoDetailUiState.Loading)
    val uiState: StateFlow<PhotoDetailUiState> = _uiState.asStateFlow()

    fun getPhoto(photoId: Int) {
        viewModelScope.launch {
            photoRepository.getPhotoById(photoId)
                .onStart { _uiState.value = PhotoDetailUiState.Loading }
                .map { result ->
                    result.map { photo ->
                        val isFavourite = photo.isFavourite
                        photo.photo.toPhotoUi(isFavourite)
                    }
                }.catch { exception ->
                    _uiState.value = PhotoDetailUiState.Error(exception.message ?: "Unknown error")
                }.collect { result ->
                    result.fold(
                        onSuccess = {
                            _uiState.value = PhotoDetailUiState.Success(it)
                        },
                        onFailure = {
                            _uiState.value = PhotoDetailUiState.Error(it.message ?: "Unknown error")
                        }
                    )
                }

        }
    }

    fun toggleFavorite(photoId: Int, isFavourite: Boolean) {
        viewModelScope.launch(ioDispatcher) {
            photoRepository.updatePhoto(photoId, isFavourite)
        }
    }
}

sealed interface PhotoDetailUiState {
    data class Success(val photo: PhotoUi) : PhotoDetailUiState
    data class Error(val message: String) : PhotoDetailUiState
    object Loading : PhotoDetailUiState
}