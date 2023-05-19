package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

interface GetTopRatedMoviesUseCase {

  suspend operator fun invoke(): Flow<List<VideoThumbnail>>
}
