package io.filmtime.feature.cast.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.filmtime.data.model.Result.Failure
import io.filmtime.data.model.Result.Success
import io.filmtime.domain.tmdb.person.GetPersonByIDUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class CastDetailViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getPersonByIDUseCase: GetPersonByIDUseCase,
) : ViewModel() {

  private val personId: Int = savedStateHandle["person_id"] ?: throw IllegalStateException("personId is required")

  private val _state: MutableStateFlow<CastDetailState> = MutableStateFlow(CastDetailState())
  val state = _state.asStateFlow()

  init {
    viewModelScope.launch { getPerson() }
  }

  private suspend fun getPerson() {
    _state.update { it.copy(isLoading = true) }
    when (val result = getPersonByIDUseCase(personId)) {
      is Failure -> TODO()
      is Success -> {
        _state.update { it.copy(isLoading = false, person = result.data) }
      }
    }
  }
}
