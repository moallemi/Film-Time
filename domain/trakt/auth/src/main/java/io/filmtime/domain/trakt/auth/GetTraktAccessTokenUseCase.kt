package io.filmtime.domain.trakt.auth

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktTokens

interface GetTraktAccessTokenUseCase {

  suspend operator fun invoke(code: String): Result<TraktTokens, GeneralError>
}
