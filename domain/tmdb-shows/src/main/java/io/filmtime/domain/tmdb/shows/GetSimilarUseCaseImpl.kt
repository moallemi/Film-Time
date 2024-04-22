package io.filmtime.domain.tmdb.shows

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.tmdb.shows.TmdbShowsRepository
import javax.inject.Inject

class GetSimilarUseCaseImpl @Inject constructor(private val tmdbMovieRepository: TmdbShowsRepository) :
  GetSimilarUseCase {
  override suspend fun invoke(movieId: Int): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMovieRepository.getSimilar(movieId)
}
