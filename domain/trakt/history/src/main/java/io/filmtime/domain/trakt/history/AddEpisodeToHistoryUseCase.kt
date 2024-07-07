package io.filmtime.domain.trakt.history

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

interface AddEpisodeToHistoryUseCase {

  suspend operator fun invoke(
    tmdbId: Int,
    seasonNumber: Int,
    episodeNumber: Int,
  ): Result<Unit, GeneralError>
}
