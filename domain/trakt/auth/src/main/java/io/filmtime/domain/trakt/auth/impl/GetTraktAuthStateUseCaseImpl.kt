package io.filmtime.domain.trakt.auth.impl

import io.filmtime.data.storage.trakt.TraktAuthLocalSource
import io.filmtime.domain.trakt.auth.GetTraktAuthStateUseCase
import io.filmtime.domain.trakt.auth.model.TraktAuthState
import io.filmtime.domain.trakt.auth.model.TraktAuthState.LoggedIn
import io.filmtime.domain.trakt.auth.model.TraktAuthState.SignedOut
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class GetTraktAuthStateUseCaseImpl @Inject constructor(
  private val traktAuthLocalSource: TraktAuthLocalSource,
) : GetTraktAuthStateUseCase {
  override suspend fun invoke(): Flow<TraktAuthState> =
    traktAuthLocalSource.tokens.map {
      if (it == null) {
        SignedOut
      } else {
        LoggedIn
      }
    }
}
