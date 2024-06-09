package io.filmtime.feature.trakt.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.GeneralError
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
  private val getTraktAccessTokenUseCase: GetTraktAccessTokenUseCase,
  private val getTraktAuthStateUseCase: GetTraktAuthStateUseCase,
) : ViewModel() {

  private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(value = LoginState.Initial)
  val loginState = _loginState.asStateFlow()

  private val _traktState: MutableStateFlow<Boolean> = MutableStateFlow(false)
  val traktState = _traktState.asStateFlow()

  init {
    viewModelScope.launch {
      collectAuthState()
    }
  }

  private suspend fun collectAuthState() {
    getTraktAuthStateUseCase().collect {
      _traktState.value = it
    }
  }

  fun getAccessToken(code: String) = viewModelScope.launch {
    _loginState.update { LoginState.Loading }
    when (val result = getTraktAccessTokenUseCase(code)) {
      is Result.Failure -> {
        _loginState.update { LoginState.Failed(result.error) }
      }
      is Result.Success -> {
        _loginState.update { LoginState.Success }
      }
    }
  }
}

sealed class LoginState {
  data object Initial : LoginState()

  data object Loading : LoginState()

  data class Failed(val error: GeneralError) : LoginState()

  data object Success : LoginState()
}
