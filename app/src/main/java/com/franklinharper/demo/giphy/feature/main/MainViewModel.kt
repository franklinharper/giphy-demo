package com.franklinharper.demo.giphy.feature.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.franklinharper.demo.giphy.common.ErrorType
import com.franklinharper.demo.giphy.data.Repository
import com.franklinharper.demo.giphy.data.domain.Gif
import com.laimiux.lce.UCE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


// The purpose of the "plumbing" code below is only to make the "application level" code
// easier to read. In a real app the plumbing code would NOT be repeated in each ViewModel.

// The UCE type used below represents the following states.
//   * Loading,
//   * Content,
//   * Error
//
//   In this case:
//   * the loading type is Unit (no extra data is required for the loading state).
//   * the content type is Gif,
//   * the error type is TypeOfError.
//
// This generic type isn't as easy to read as I would like. And having to add explanatory
// comments like the one above can be a "code smell".
//
// In a real app I'd implement a "loading content error" type within the app.
// But to save time I'll just pull in a library and clean up the application
// level code using a typealias.
typealias TrendingResult = UCE<List<Gif>, ErrorType>

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // More hacky "plumbing" code.
    private val loadingState = UCE.loading()

    // Hack: I'm NOT even trying to figure out what the real cause of the error is.
    private val errorState = UCE.error(ErrorType.OTHER)

    private fun contents(gifs: List<Gif>) = UCE.content(gifs)

    //
    // Done with the hacks; below is what real "application" code would look like.
    //
    private val mutableTrendingResults = MutableLiveData<TrendingResult>()
    val trendingResults: LiveData<TrendingResult>
        get() = mutableTrendingResults

    fun loadData() {
        mutableTrendingResults.value = loadingState
        viewModelScope.launch {
            runCatching {
                repository.loadTrendingGifs(viewModelScope)
            }.onFailure { throwable ->
                Timber.e(throwable)
                mutableTrendingResults.postValue(errorState)
            }.onSuccess { trendingGifs ->
                mutableTrendingResults.postValue(contents(trendingGifs ?: emptyList()))
            }
        }
    }
}