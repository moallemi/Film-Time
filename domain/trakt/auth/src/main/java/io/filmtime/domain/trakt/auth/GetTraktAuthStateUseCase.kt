package io.filmtime.domain.trakt.auth

import kotlinx.coroutines.flow.Flow

interface GetTraktAuthStateUseCase {

  suspend operator fun invoke(): Flow<TraktAuthState>
}

sealed class TraktAuthState {
  data object Initial : TraktAuthState()
  data object LoggedIn : TraktAuthState()

  data object SignedOut : TraktAuthState()
}
