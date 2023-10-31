package io.filmtime.feature.home
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.domain.tmdb.movies.GetTrendingMoviesUseCase
import io.filmtime.domain.tmdb.shows.GetTrendingShowsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
  private val getTrendingMovies: GetTrendingMoviesUseCase,
  private val getTrendingShows: GetTrendingShowsUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow(HomeUiState(isLoading = false))
  val state = _state.asStateFlow()


  init {

    loadTrendingMovies()
    loadTrendingShows()

  }

 private  fun loadTrendingMovies() {
    viewModelScope.launch {
      getTrendingMovies()
        .onStart {
          _state.update { state -> state.copy(isLoading = true) }

        }
        .onCompletion { _state.update { state -> state.copy(isLoading = false) } }
        .onEach { result ->
          when (result) {
            is Success -> {
              _state.update { state ->
                state.copy(videoSections = state.videoSections + listOf(VideoSection("Trending Movies", result.data)))
              }


            }

            is Failure -> {
              _state.update { state ->
                state.copy(error = result.error, isLoading = false)

              }

            }
          }

        }
        .collect()
    }
  }

   private fun loadTrendingShows() {
      viewModelScope.launch {
        getTrendingShows()
          .onStart {
            _state.update { state -> state.copy(isLoading = true) }

          }
          .onCompletion { _state.update { state -> state.copy(isLoading = false) } }
          .onEach { result ->
            when (result) {
              is Success -> {
                _state.update { state ->
                  state.copy(
                    isLoading = true,
                    videoSections = state.videoSections + listOf(VideoSection("Trending Shows", result.data))
                  )
                }

              }

              is Failure -> {
                _state.update { state ->
                  state.copy(error = result.error, isLoading = false, videoSections = emptyList())

                }

              }


            }
          }
          .collect()
      }
    }

 fun retry (){
   _state.update { state-> state.copy(isLoading = true, error = null) }
       loadTrendingMovies()
       loadTrendingShows()
 }

}
