package io.filmtime.domain.trakt.history

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.TraktEpisodeHistory

interface IsShowWatchedUseCase {

  suspend operator fun invoke(
    tmdbId: Int,
    seasonNumber: Int,
  ): Result<Map<Int, List<TraktEpisodeHistory>>, GeneralError>
}
