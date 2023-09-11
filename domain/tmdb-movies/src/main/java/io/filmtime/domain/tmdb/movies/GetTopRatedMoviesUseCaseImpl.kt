package io.filmtime.domain.tmdb.movies

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetTopRatedMoviesUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : GetTopRatedMoviesUseCase {

  override suspend fun invoke(): Flow<Result<List<VideoThumbnail>, GeneralError>> = flow {
    emit(tmdbMovieRepository.getTopRatedMovies())
  }
}
