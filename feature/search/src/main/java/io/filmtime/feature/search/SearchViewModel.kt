package io.filmtime.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType
import io.filmtime.domain.tmdb.search.SearchTmdbUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
  private val searchUseCase: SearchTmdbUseCase,
) : ViewModel() {

  private val _state: MutableStateFlow<PagingData<SearchResult>> = MutableStateFlow(PagingData.empty())
  val state = _state.asStateFlow()

  fun search(text: String, type: SearchType) = viewModelScope.launch {
    searchUseCase(text, type)
      .distinctUntilChanged()
      .cachedIn(viewModelScope)
      .collect {
        _state.value = it
      }
//    currentQuery.value = text
  }

  fun loadMore() {}
}
