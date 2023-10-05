package io.filmtime.feature.trakt.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.domain.trakt.auth.GetTraktAccessTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TraktLoginViewModel @Inject constructor(
  private val getTraktAccessTokenUseCase: GetTraktAccessTokenUseCase,
) : ViewModel() {


  private val _loginState: MutableStateFlow<LoginState> = MutableStateFlow(value = LoginState.Initial)
  val loginState = _loginState.asStateFlow()


  fun getAccessToken(code: String) = viewModelScope.launch {
    _loginState.update { LoginState.Loading }
    when(val result = getTraktAccessTokenUseCase(code)) {
      is Result.Failure -> {
        _loginState.update { LoginState.Failed(result.error) }
        println(result.error)
      }
      is Result.Success -> {
        _loginState.update { LoginState.Success }
        println(result.data)
      }
    }
  }

}

sealed class LoginState {
  data object Initial: LoginState()

  data object Loading: LoginState()

  data class Failed(val error: GeneralError): LoginState()

  data object Success: LoginState()
}
