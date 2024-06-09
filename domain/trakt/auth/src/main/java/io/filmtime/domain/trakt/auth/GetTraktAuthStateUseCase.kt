package io.filmtime.domain.trakt.auth

import kotlinx.coroutines.flow.Flow

interface GetTraktAuthStateUseCase {

  suspend operator fun invoke(): Flow<Boolean>
}
