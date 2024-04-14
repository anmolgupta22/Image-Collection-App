package com.example.imagecollectionapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.imagecollectionapp.repository.UnsplashRepository
import com.example.imagecollectionapp.util.ResponseResult
import com.example.imagecollectionapp.util.ViewState
import com.example.imagecollectionapp.util.shareWhileObserved
import com.google.gson.JsonObject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageViewModel @Inject constructor(
    private val repository: UnsplashRepository,
) : ViewModel() {

    private val _getAllPhotosFlow = MutableSharedFlow<ViewState<List<JsonObject>>>()
    val getAllPhotosFlow: SharedFlow<ViewState<List<JsonObject>>> =
        _getAllPhotosFlow.shareWhileObserved(viewModelScope)

    fun getAllPhotos(page: Int?) {
        viewModelScope.launch {
            // Emit loading state to notify observers that data fetching has started
            _getAllPhotosFlow.emit(ViewState.loading())

            // Fetch photos from the repository asynchronously
            val viewState = when (val responseState =
                repository.getUnsplashImages(page)) {
                is ResponseResult.Success -> ViewState.success(responseState.data) // Emit success state with fetched photos
                is ResponseResult.Error -> ViewState.failed(responseState.message) // Emit error state with error message
                is ResponseResult.NetworkException -> ViewState.NetworkFailed(responseState.networkError) // Emit network error state
                is ResponseResult.ErrorException -> ViewState.exceptionError(responseState.exception) // Emit generic error state
                else -> ViewState.exceptionError(responseState.toString()) // Handle unknown state
            }

            // Emit the final view state based on the result of the repository call
            _getAllPhotosFlow.emit(viewState)
        }
    }
}
