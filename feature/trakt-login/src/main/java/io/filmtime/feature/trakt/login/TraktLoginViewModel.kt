package io.filmtime.feature.trakt.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.Result
import io.filmtime.domain.trakt.auth.GetTraktAccessTokenUseCase
import io.filmtime.domain.trakt.auth.GetTraktAuthStateUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraktLoginViewModel @Inject constructor(
  savedStateHandle: SavedStateHandle,
  private val getTraktAccessTokenUseCase: GetTraktAccessTokenUseCase,
  private val getTraktAuthStateUseCase: GetTraktAuthStateUseCase,
) : ViewModel() {

  private val code: String? = savedStateHandle.get<String>("code")
  private val error: String? = savedStateHandle.get<String>("error")

  private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(value = LoginState.Loading)
  val loginState = _loginState.asStateFlow()

  private val _isLoggedIn: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val isLoggedIn = _isLoggedIn.asStateFlow()

  init {
    viewModelScope.launch {
      collectAuthState()
    }
    viewModelScope.launch {
      if (code != null) {
        getAccessToken(code)
      } else if (error != null) {
        _loginState.update { LoginState.Failed }
      }
    }
  }

  private suspend fun collectAuthState() {
    getTraktAuthStateUseCase().collect {
      _isLoggedIn.value = it
    }
  }

  private fun getAccessToken(code: String) = viewModelScope.launch {
    _loginState.update { LoginState.Loading }
    when (getTraktAccessTokenUseCase(code)) {
      is Result.Failure -> {
        _loginState.update { LoginState.Failed }
      }

      is Result.Success -> {
        _loginState.update { LoginState.Success }
      }
    }
  }
}

enum class LoginState {
  Loading,
  Success,
  Failed,
}
