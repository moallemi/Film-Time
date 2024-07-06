package io.filmtime.domain.tmdb.shows.impl

import io.filmtime.data.model.EpisodeThumbnail
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import io.filmtime.domain.tmdb.shows.GetEpisodesBySeasonUseCase
import javax.inject.Inject

internal class GetEpisodesBySeasonUseCaseImpl @Inject constructor(
  private val showsRepository: TmdbShowsRepository,
) : GetEpisodesBySeasonUseCase {

  override suspend fun invoke(showId: Int, seasonNumber: Int): Result<List<EpisodeThumbnail>, GeneralError> =
    showsRepository.episodesBySeason(showId, seasonNumber)
}
