package io.filmtime.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.domain.trakt.auth.GetTraktAuthStateUseCase
import io.filmtime.domain.trakt.auth.LogoutTraktUseCase
import io.filmtime.domain.trakt.auth.model.TraktAuthState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
  private val getTraktAuthState: GetTraktAuthStateUseCase,
  private val logoutTrakt: LogoutTraktUseCase,
) : ViewModel() {

  private val _state = MutableStateFlow(SettingsUiState())
  val state = _state.asStateFlow()

  init {
    observeTraktState()
  }

  private fun observeTraktState() = viewModelScope.launch {
    getTraktAuthState()
      .onEach { state ->
        _state.update { it.copy(isTraktLoggedIn = state == TraktAuthState.LoggedIn) }
      }
      .collect()
  }

  fun traktLogout() = viewModelScope.launch {
    logoutTrakt()
  }
}
