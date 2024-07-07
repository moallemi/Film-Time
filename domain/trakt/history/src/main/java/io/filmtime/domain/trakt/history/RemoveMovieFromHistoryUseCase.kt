package io.filmtime.domain.trakt.history

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

interface RemoveMovieFromHistoryUseCase {

  suspend operator fun invoke(id: Int): Result<Unit, GeneralError>
}
