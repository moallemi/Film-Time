package io.filmtime.data.tmdb.shows

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail

interface TmdbShowsRepository {

  suspend fun getTrendingShows(): Result<List<VideoThumbnail>, GeneralError>
}
