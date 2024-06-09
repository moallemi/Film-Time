package io.fimltime.data.tmdb.movies

import androidx.paging.PagingData
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow

interface TmdbMovieRepository {

  suspend fun getMovieDetails(movieId: Int): Flow<Result<VideoDetail, GeneralError>>

  suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getPopularMovies(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getTopRatedMovies(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun getNowPlayingMovies(): Result<List<VideoThumbnail>, GeneralError>

  suspend fun upcomingMovies(): Result<List<VideoThumbnail>, GeneralError>

  fun moviesStream(movieListType: MovieListType): Flow<PagingData<VideoThumbnail>>

  suspend fun credits(movieId: Int): Result<List<Person>, GeneralError>

  suspend fun getSimilar(movieId: Int): Result<List<VideoThumbnail>, GeneralError>
}

enum class MovieListType {
  Trending,
  Popular,
  TopRated,
  NowPlaying,
  Upcoming,
}
