package io.filmtime.feature.trakt.login

internal data class TraktLoginUiState(
  val loginState: LoginState = LoginState.Loading,
  val isLoggedIn: Boolean = false,
)

internal enum class LoginState {
  Loading,
  Success,
  Failed,
}
