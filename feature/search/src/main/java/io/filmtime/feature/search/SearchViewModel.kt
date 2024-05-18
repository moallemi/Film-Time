package io.filmtime.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : ViewModel() {

  private val _state = MutableStateFlow(SearchUiState())
  val state = _state.asStateFlow()

  fun search(text: String, type: SearchType) {
    _state.update { it.copy(loading = true) }

    viewModelScope.launch {
      val data = tmdbMovieRepository.searchMovies(text)
      when (data) {
        is Failure -> TODO()
        is Success -> _state.update {
          it.copy(loading = false, data = data.data, hasResult = data.data.isNotEmpty())
        }
      }
    }
  }
}
