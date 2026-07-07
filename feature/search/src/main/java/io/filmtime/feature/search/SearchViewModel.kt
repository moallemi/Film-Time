package io.filmtime.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType
import io.filmtime.domain.tmdb.search.SearchTmdbUseCase
import io.filmtime.feature.search.SearchAction.Search
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
internal class SearchViewModel @Inject constructor(
  private val searchUseCase: SearchTmdbUseCase,
) : ViewModel() {

  private val pendingActions = MutableSharedFlow<SearchAction>()

  private val _pagedList: MutableStateFlow<PagingData<SearchResult>> = MutableStateFlow(PagingData.empty())
  val pagedList = _pagedList.asStateFlow()

  init {
    collectActions()
  }

  fun submitAction(action: SearchAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is Search -> search(action.query, action.type)
      }
    }
  }

  private fun search(text: String, type: SearchType) = launch {
    searchUseCase(text, type)
      .distinctUntilChanged()
      .cachedIn(viewModelScope)
      .collect {
        _pagedList.value = it
      }
  }
}
