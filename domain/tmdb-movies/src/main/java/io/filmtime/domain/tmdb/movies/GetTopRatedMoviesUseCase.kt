package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

interface GetTopRatedMoviesUseCase {

  suspend operator fun invoke(): Flow<Result<List<VideoThumbnail>, GeneralError>>
}
