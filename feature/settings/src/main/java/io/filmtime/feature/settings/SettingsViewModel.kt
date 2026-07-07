package io.filmtime.feature.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.domain.trakt.auth.GetTraktAuthStateUseCase
import io.filmtime.domain.trakt.auth.LogoutTraktUseCase
import io.filmtime.feature.settings.SettingsAction.TraktLogout
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class SettingsViewModel @Inject constructor(
  private val getTraktAuthState: GetTraktAuthStateUseCase,
  private val logoutTrakt: LogoutTraktUseCase,
) : ViewModel() {

  private val pendingActions = MutableSharedFlow<SettingsAction>()

  private val _state = MutableStateFlow(SettingsUiState())
  val state = _state.asStateFlow()

  init {
    collectActions()
    observeTraktState()
  }

  fun submitAction(action: SettingsAction) = launch { pendingActions.emit(action) }

  private fun collectActions() = launch {
    pendingActions.collect { action ->
      when (action) {
        is TraktLogout -> logoutTrakt()
      }
    }
  }

  private fun observeTraktState() = launch {
    getTraktAuthState()
      .onEach { state ->
        _state.update { it.copy(isTraktLoggedIn = state) }
      }
      .collect()
  }
}
