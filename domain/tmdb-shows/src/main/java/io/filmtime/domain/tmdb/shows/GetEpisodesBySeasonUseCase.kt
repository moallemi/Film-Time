package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result

interface GetEpisodesBySeasonUseCase {

  suspend operator fun invoke(showId: Int, seasonNumber: Int): Result<List<EpisodeThumbnail>, GeneralError>
}
