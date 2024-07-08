package io.filmtime.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.data.model.SearchType
import io.filmtime.domain.tmdb.search.SearchTmdbUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
  private val tmdbSearchUseCase: SearchTmdbUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow(SearchUiState())
  val state = _state.asStateFlow()

  fun search(text: String, type: SearchType) {
    _state.update { it.copy(loading = true) }

    viewModelScope.launch {
      val data = tmdbSearchUseCase(text, type)
      when (data) {
        is Failure -> {
          _state.update { it.copy(error = data.error.toString(), loading = false) }
        }
        is Success -> _state.update {
          it.copy(loading = false, data = data.data, hasResult = data.data.isNotEmpty(), error = null)
        }
      }
    }
  }
}
