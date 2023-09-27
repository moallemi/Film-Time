package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import javax.inject.Inject

internal class GetMovieDetailsUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : GetMovieDetailsUseCase {

  override suspend fun invoke(movieId: Int): Result<VideoDetail, GeneralError> =
    tmdbMovieRepository.getMovieDetails(movieId)
}
