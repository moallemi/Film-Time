package io.filmtime.domain.trakt.auth.model

sealed class TraktAuthState {
  data object Initial : TraktAuthState()
  data object LoggedIn : TraktAuthState()
  data object SignedOut : TraktAuthState()
}
