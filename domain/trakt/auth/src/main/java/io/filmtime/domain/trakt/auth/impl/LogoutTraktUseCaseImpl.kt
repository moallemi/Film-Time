package io.filmtime.domain.trakt.auth.impl

import io.filmtime.data.trakt.auth.TraktAuthRepository
import io.filmtime.domain.trakt.auth.LogoutTraktUseCase
import javax.inject.Inject

class LogoutTraktUseCaseImpl @Inject constructor(
  private val traktAuthRepository: TraktAuthRepository,
) : LogoutTraktUseCase {

  override suspend fun invoke() =
    traktAuthRepository.logout()
}
