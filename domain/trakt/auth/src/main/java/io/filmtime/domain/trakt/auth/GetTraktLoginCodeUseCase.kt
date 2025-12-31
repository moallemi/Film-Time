package io.filmtime.domain.trakt.auth

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktCodeLogin

interface GetTraktLoginCodeUseCase {

  suspend operator fun invoke(): Result<TraktCodeLogin, GeneralError>
}
