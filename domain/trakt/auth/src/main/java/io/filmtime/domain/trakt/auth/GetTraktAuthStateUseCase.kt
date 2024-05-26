package io.filmtime.domain.trakt.auth

import io.filmtime.domain.trakt.auth.model.TraktAuthState
import kotlinx.coroutines.flow.Flow

interface GetTraktAuthStateUseCase {

  suspend operator fun invoke(): Flow<TraktAuthState>
}
