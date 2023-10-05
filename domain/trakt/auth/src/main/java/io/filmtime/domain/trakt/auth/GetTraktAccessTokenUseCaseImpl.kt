package io.filmtime.domain.trakt.auth

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens
import io.filmtime.data.trakt_auth.TraktAuthRepository
import javax.inject.Inject

class GetTraktAccessTokenUseCaseImpl @Inject constructor(
  private val traktRepository: TraktAuthRepository,
) : GetTraktAccessTokenUseCase {
  override suspend fun invoke(code: String): Result<TraktTokens, GeneralError> =
    traktRepository.getAccessTokenByCode(code)

}
