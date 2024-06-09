package io.filmtime.domain.trakt.auth.impl

import io.filmtime.data.trakt.auth.TraktAuthRepository
import io.filmtime.domain.trakt.auth.GetTraktAuthStateUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetTraktAuthStateUseCaseImpl @Inject constructor(
  private val traktAuthRepository: TraktAuthRepository,
) : GetTraktAuthStateUseCase {

  override suspend fun invoke(): Flow<Boolean> =
    traktAuthRepository.isLoggedIn
}
