package io.filmtime.domain.tmdb.movies.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.domain.tmdb.movies.GetSimilarMoviesUseCase
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import javax.inject.Inject

class GetSimilarMoviesUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : GetSimilarMoviesUseCase {

  override suspend fun invoke(movieId: Int): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMovieRepository.getSimilar(movieId)
}
