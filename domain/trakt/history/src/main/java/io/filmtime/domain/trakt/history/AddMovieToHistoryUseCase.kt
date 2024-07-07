package io.filmtime.domain.trakt.history

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

interface AddMovieToHistoryUseCase {

  suspend operator fun invoke(traktId: Int): Result<Unit, GeneralError>
}
