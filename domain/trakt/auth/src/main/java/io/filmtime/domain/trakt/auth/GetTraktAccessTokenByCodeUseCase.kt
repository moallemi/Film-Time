package io.filmtime.domain.trakt.auth

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens

interface GetTraktAccessTokenByCodeUseCase {

  suspend operator fun invoke(deviceCode: String): Result<TraktTokens, GeneralError>
}
