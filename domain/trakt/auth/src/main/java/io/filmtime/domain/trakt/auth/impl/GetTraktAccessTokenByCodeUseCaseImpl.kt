package io.filmtime.domain.trakt.auth.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens
import io.filmtime.data.trakt.auth.TraktAuthRepository
import io.filmtime.domain.trakt.auth.GetTraktAccessTokenByCodeUseCase
import javax.inject.Inject

class GetTraktAccessTokenByCodeUseCaseImpl @Inject constructor(
  private val repository: TraktAuthRepository,
) : GetTraktAccessTokenByCodeUseCase {
  override suspend fun invoke(deviceCode: String): Result<TraktTokens, GeneralError> =
    repository.getAccessTokenByDeviceCode(deviceCode)
}
