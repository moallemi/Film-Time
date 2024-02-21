package io.filmtime.domain.tmdb.movies.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoListType.AiringToday
import io.filmtime.data.model.VideoListType.NowPlaying
import io.filmtime.data.model.VideoListType.OnTheAir
import io.filmtime.data.model.VideoListType.Popular
import io.filmtime.data.model.VideoListType.TopRated
import io.filmtime.data.model.VideoListType.Trending
import io.filmtime.data.model.VideoListType.Upcoming
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
import io.fimltime.data.tmdb.movies.TmdbMovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

internal class GetMoviesListUseCaseImpl @Inject constructor(
  private val tmdbMovieRepository: TmdbMovieRepository,
) : GetMoviesListUseCase {

  override suspend fun invoke(videoListType: VideoListType): Flow<Result<List<VideoThumbnail>, GeneralError>> =
    when (videoListType) {
      Trending -> tmdbMovieRepository.getTrendingMovies()
      Upcoming -> tmdbMovieRepository.upcomingMovies()
      NowPlaying -> tmdbMovieRepository.getNowPlayingMovies()
      Popular -> tmdbMovieRepository.getPopularMovies()
      TopRated -> tmdbMovieRepository.getTopRatedMovies()
      OnTheAir -> throw IllegalStateException("OnTheAir not supported")
      AiringToday -> throw IllegalStateException("AiringToday not supported")
    }.let(::flowOf)
}
