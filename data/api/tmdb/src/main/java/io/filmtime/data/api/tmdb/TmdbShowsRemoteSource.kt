package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail

interface TmdbShowsRemoteSource {

  suspend fun getShowDetails(showId: Int): Result<VideoDetail, GeneralError>

  suspend fun getTrendingShows(): Result<List<VideoThumbnail>, GeneralError>
}
