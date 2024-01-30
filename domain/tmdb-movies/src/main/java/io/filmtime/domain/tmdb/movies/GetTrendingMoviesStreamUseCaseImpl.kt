package io.filmtime.domain.tmdb.movies

import androidx.paging.PagingData
import io.filmtime.data.model.VideoThumbnail
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class GetTrendingMoviesStreamUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : GetTrendingMoviesStreamUseCase {

  override fun invoke(): Flow<PagingData<VideoThumbnail>> =
    tmdbMovieRepository.getTrendingMoviesStream()
}
