package io.filmtime.domain.tmdb.movies

import androidx.paging.PagingData
import io.filmtime.data.model.VideoThumbnail
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTrendingMoviePagingUsecaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository
):GetTrendingMoviePagingUsecase {
  override  fun invoke(): Flow<PagingData<VideoThumbnail>> {
   return tmdbMovieRepository.getAllTrendingMovies()
  }

}
