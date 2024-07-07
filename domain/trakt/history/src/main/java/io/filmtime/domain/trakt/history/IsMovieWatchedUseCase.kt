package io.filmtime.domain.trakt.history

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktMovieHistory

interface IsMovieWatchedUseCase {

  suspend operator fun invoke(tmdbId: Int): Result<TraktMovieHistory, GeneralError>
}
