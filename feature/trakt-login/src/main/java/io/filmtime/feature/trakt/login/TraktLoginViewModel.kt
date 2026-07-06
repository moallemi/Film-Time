package io.filmtime.feature.trakt.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.core.ui.common.extensions.launch
import io.filmtime.data.model.Result
import io.filmtime.domain.trakt.auth.GetTraktAccessTokenUseCase
import io.filmtime.domain.trakt.auth.GetTraktAuthStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
internal class TraktLoginViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getTraktAccessTokenUseCase: GetTraktAccessTokenUseCase,
  private val getTraktAuthStateUseCase: GetTraktAuthStateUseCase,
) : ViewModel() {

  private val code: String? = savedStateHandle.get<String>("code")
  private val error: String? = savedStateHandle.get<String>("error")

  private val _state = MutableStateFlow(TraktLoginUiState())
  val state = _state.asStateFlow()

  init {
    launch {
      collectAuthState()
    }
    launch {
      if (code != null) {
        getAccessToken(code)
      } else if (error != null) {
        _state.update { it.copy(loginState = LoginState.Failed) }
      }
    }
  }

  private suspend fun collectAuthState() {
    getTraktAuthStateUseCase().collect { isLoggedIn ->
      _state.update { it.copy(isLoggedIn = isLoggedIn) }
    }
  }

  private fun getAccessToken(code: String) = launch {
    _state.update { it.copy(loginState = LoginState.Loading) }
    when (getTraktAccessTokenUseCase(code)) {
      is Result.Failure -> {
        _state.update { it.copy(loginState = LoginState.Failed) }
      }

      is Result.Success -> {
        _state.update { it.copy(loginState = LoginState.Success) }
      }
    }
  }
}
