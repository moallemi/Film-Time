package io.filmtime.domain.trakt.auth.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktCodeLogin
import io.filmtime.data.trakt.auth.TraktAuthRepository
import io.filmtime.domain.trakt.auth.GetTraktLoginCodeUseCase
import javax.inject.Inject

internal class GetTraktLoginCodeUseCaseImpl @Inject constructor(
  private val repository: TraktAuthRepository,
) : GetTraktLoginCodeUseCase {
  override suspend fun invoke(): Result<TraktCodeLogin, GeneralError> =
    repository.getLoginCode()
}
