package com.brian.android_mvvm_architecture.ui.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brian.android_mvvm_architecture.data.model.FavouritePhoto
import com.brian.android_mvvm_architecture.data.repository.PhotoRepository
import com.brian.android_mvvm_architecture.ui.mapper.toPhotoUi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@HiltViewModel
class PhotosViewModel @Inject constructor(private val photoRepository: PhotoRepository) :
    ViewModel() {

    private val _uiState = MutableStateFlow<PhotosUiState>(PhotosUiState.Loading)
    val uiState: StateFlow<PhotosUiState> = _uiState.asStateFlow()

    private val _isShowingFavouritePhotos = MutableStateFlow(false)
    val isShowingFavouritePhotos: StateFlow<Boolean> = _isShowingFavouritePhotos.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    init {
        getPhotos()
    }

    fun getPhotos() {
        viewModelScope.launch {
            combine(_isShowingFavouritePhotos, _searchQuery, ::Pair)
                .onStart { _uiState.value = PhotosUiState.Loading }
                .debounce(300)
                .flatMapLatest { (isFavourite, query) ->
                    doFilterPhotoFlow(isFavourite, query)
                }
                .map { photos ->
                    photos.map { it.photo.toPhotoUi(it.isFavourite) }
                }
                .catch { ex -> _uiState.value = PhotosUiState.Error(ex.message ?: "Unknown error") }
                .collect { photos -> _uiState.value = PhotosUiState.Success(photos) }
        }

    }

    private fun doFilterPhotoFlow(isFavourite: Boolean, query: String): Flow<List<FavouritePhoto>> {
        val sourceFlow: Flow<Result<List<FavouritePhoto>>> = if (isFavourite) {
            photoRepository.getFavouritePhotos()
        } else {
            photoRepository.getPhotos()
        }
        return sourceFlow
            .map { result ->
                result.getOrElse { emptyList() }
                    .filter { it.photo.title.contains(query, ignoreCase = true) }
            }
    }

    fun toggleFavourite() {
        _isShowingFavouritePhotos.value = !_isShowingFavouritePhotos.value
    }

    fun searchPhotos(query: String) {
        _searchQuery.value = query
    }
}


sealed interface PhotosUiState {
    data class Success(val photos: List<PhotoUi>) : PhotosUiState
    data class Error(val message: String) : PhotosUiState
    object Loading : PhotosUiState
}

data class PhotoUi(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val thumbnailUrl: String,
    val isFavourite: Boolean,
)