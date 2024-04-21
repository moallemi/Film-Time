package io.filmtime.domain.trakt.auth.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens
import io.filmtime.data.trakt.auth.TraktAuthRepository
import io.filmtime.domain.trakt.auth.GetTraktAccessTokenUseCase
import javax.inject.Inject

internal class GetTraktAccessTokenUseCaseImpl @Inject constructor(
  private val traktRepository: TraktAuthRepository,
) : GetTraktAccessTokenUseCase {
  override suspend fun invoke(code: String): Result<TraktTokens, GeneralError> =
    traktRepository.getAccessTokenByCode(code)
}
