package io.filmtime.domain.tmdb.movies

import androidx.paging.PagingData
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

/**
 * This interface defines a use case for getting a stream of trending movies.
 * The stream is represented as a Flow of PagingData, which is a container for
 * paged data from the TMDB API. Each page contains a list of VideoThumbnail objects,
 * which represent the thumbnail data for a movie.
 */
interface GetTrendingMoviesStreamUseCase {

  /**
   * This function is the entry point for the use case. It does not take any parameters.
   * When this function is invoked, it returns a Flow of PagingData of VideoThumbnail.
   * Each VideoThumbnail represents the thumbnail data for a movie.
   *
   * @return Flow<PagingData<VideoThumbnail>>: a Flow that emits pages of trending movies.
   */
  operator fun invoke(): Flow<PagingData<VideoThumbnail>>
}
