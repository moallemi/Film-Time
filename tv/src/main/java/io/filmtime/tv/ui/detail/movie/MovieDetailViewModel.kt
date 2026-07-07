package io.filmtime.tv.ui.detail.movie

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.core.ui.common.toUiMessage
import io.filmtime.data.model.VideoType.Movie
import io.filmtime.domain.bookmarks.AddBookmarkUseCase
import io.filmtime.domain.bookmarks.DeleteBookmarkUseCase
import io.filmtime.domain.bookmarks.ObserveBookmarkUseCase
import io.filmtime.domain.stream.GetStreamInfoUseCase
import io.filmtime.domain.tmdb.movies.GetMovieCollectionUseCase
import io.filmtime.domain.tmdb.movies.GetMovieDetailsUseCase
import io.filmtime.domain.trakt.GetRatingsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class MovieDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getMovieDetail: GetMovieDetailsUseCase,
  private val getStreamInfo: GetStreamInfoUseCase,
  private val addBookmark: AddBookmarkUseCase,
  private val deleteBookmark: DeleteBookmarkUseCase,
  private val observeBookmark: ObserveBookmarkUseCase,
  private val getRatings: GetRatingsUseCase,
  private val getCollection: GetMovieCollectionUseCase,
) : ViewModel() {

  private val videoId: Int = savedStateHandle.toRoute<MovieDetail>().videoId

  private val _state = MutableStateFlow(MovieDetailState())
  val state = _state.asStateFlow()

  private val pendingActions = MutableSharedFlow<MovieDetailAction>()

  private val _navigationEvents = MutableSharedFlow<MovieDetailNavigationEvent>()
  val navigationEvents = _navigationEvents.asSharedFlow()

  init {
    collectActions()
    loadMovieDetail()
    observeBookmark()
  }

  fun submitAction(action: MovieDetailAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is MovieDetailAction.Play -> loadStreamInfo()
        is MovieDetailAction.AddBookmark -> addBookmark()
        is MovieDetailAction.RemoveBookmark -> removeBookmark()
      }
    }
  }

  private fun loadMovieDetail() = launch {
    _state.value = _state.value.copy(isLoading = true, error = null)

    getMovieDetail(videoId)
      .collect { result ->
        result.fold(
          onSuccess = { data ->
            _state.update { state ->
              state.copy(
                videoDetail = data,
                isLoading = false,
              )
            }
            loadRatings()
            loadCollection(data.collectionId?.toInt())
          },
          onFailure = { e -> _state.update { state -> state.copy(isLoading = false, error = e.toUiMessage()) } },
        )
      }
  }

  private fun loadRatings() = launch {
    _state.value.videoDetail?.ids?.tmdbId?.let { tmdbId ->
      getRatings(type = Movie, tmdbId = tmdbId)
        .fold(
          onSuccess = { ratings -> _state.update { state -> state.copy(ratings = ratings) } },
          onFailure = { error -> _state.update { state -> state.copy(error = error.toUiMessage()) } },
        )
    }
  }

  private fun loadStreamInfo() = launch {
    _state.value = _state.value.copy(isStreamLoading = true)
    getStreamInfo()
      .onEach { streamInfo ->
        _state.value = _state.value.copy(streamInfo = streamInfo, isStreamLoading = false)
        _navigationEvents.emit(MovieDetailNavigationEvent.NavigateToPlayer(streamInfo.url))
      }
      .collect()
  }

  private fun loadCollection(collectionId: Int?) = launch {
    if (collectionId == null) return@launch
    _state.value = _state.value.copy(isCollectionLoading = true)
    getCollection(collectionId)
      .fold(
        onSuccess = { collections ->
          _state.update {
            it.copy(
              isCollectionLoading = false,
              collection = collections,
            )
          }
        },
        onFailure = { error ->
          _state.update { state -> state.copy(error = error.toUiMessage()) }
        },
      )
  }

  private fun observeBookmark() = launch {
    observeBookmark(videoId, Movie)
      .onEach { isBookmarked ->
        _state.update { state ->
          state.copy(isBookmarked = isBookmarked)
        }
      }
      .collect()
  }

  private fun addBookmark() = launch {
    addBookmark(videoId, Movie)
  }

  private fun removeBookmark() = launch {
    deleteBookmark(videoId, Movie)
  }
}
