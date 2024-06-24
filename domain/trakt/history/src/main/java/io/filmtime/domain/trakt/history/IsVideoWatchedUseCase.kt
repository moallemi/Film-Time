package io.filmtime.domain.trakt.history

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktHistory

interface IsVideoWatchedUseCase {

  suspend operator fun invoke(tmdbId: Int): Result<TraktHistory, GeneralError>
}
