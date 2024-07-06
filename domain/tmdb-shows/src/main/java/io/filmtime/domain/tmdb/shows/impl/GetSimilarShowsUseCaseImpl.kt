package io.filmtime.domain.tmdb.shows.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import io.filmtime.domain.tmdb.shows.GetSimilarShowsUseCase
import javax.inject.Inject

internal class GetSimilarShowsUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbShowsRepository,
) : GetSimilarShowsUseCase {

  override suspend fun invoke(movieId: Int): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMovieRepository.similar(movieId)
}
