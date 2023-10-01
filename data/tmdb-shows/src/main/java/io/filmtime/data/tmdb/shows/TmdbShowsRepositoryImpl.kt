package io.filmtime.data.tmdb.shows

import io.filmtime.data.api.tmdb.TmdbShowsRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import javax.inject.Inject

internal class TmdbShowsRepositoryImpl @Inject constructor(
  private val tmdbShowsRemoteSource: TmdbShowsRemoteSource,
) : TmdbShowsRepository {

  override suspend fun getTrendingShows(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbShowsRemoteSource.getTrendingShows()
}
