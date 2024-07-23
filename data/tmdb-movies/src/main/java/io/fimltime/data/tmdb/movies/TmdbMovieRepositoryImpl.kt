package io.fimltime.data.tmdb.movies

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import io.filmtime.data.api.tmdb.TmdbMoviesRemoteSource
import io.filmtime.data.database.dao.MovieDetailDao
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.MovieCollection
import io.filmtime.data.model.Person
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoDetail
import io.filmtime.data.model.VideoThumbnail
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class TmdbMovieRepositoryImpl @Inject constructor(
  private val tmdbMoviesRemoteSource: TmdbMoviesRemoteSource,
  private val movieDao: MovieDetailDao,
) : TmdbMovieRepository {

  override suspend fun getMovieDetails(movieId: Int): Flow<Result<VideoDetail, GeneralError>> = flow {
//    val localData = movieDao.getMovieByTmdbId(movieId).firstOrNull()
//    if (localData != null) {
//      emit(Result.Success(localData.toMovie()))
//      // TODO: invalidate cache later
//      fetchMovieDetailsFromNetwork(movieId)
//    } else {
    val apiResult = fetchMovieDetailsFromNetwork(movieId)
    emit(apiResult)
//    }
  }

  private suspend fun fetchMovieDetailsFromNetwork(movieId: Int): Result<VideoDetail, GeneralError> {
    return when (val result = tmdbMoviesRemoteSource.movieDetails(movieId)) {
      is Result.Success -> {
        movieDao.storeMovie(result.data.toEntity())
        result
      }

      is Result.Failure -> result
    }
  }

  override suspend fun getTrendingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.trendingMovies(1)

  override suspend fun getPopularMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.popularMovies(page = 1)

  override suspend fun getTopRatedMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.topRatedMovies(page = 1)

  override suspend fun getNowPlayingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.nowPlayingMovies(page = 1)

  override suspend fun upcomingMovies(): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.upcomingMovies(page = 1)

  override fun moviesStream(movieListType: MovieListType): Flow<PagingData<VideoThumbnail>> =
    Pager(
      config = PagingConfig(
        pageSize = TmdbMoviesRemoteSource.PAGE_SIZE,
        enablePlaceholders = false,
      ),
      pagingSourceFactory = {
        MoviesPagingSource(
          tmdbMoviesRemoteSource = tmdbMoviesRemoteSource,
          movieListType = movieListType,
        )
      },
    ).flow

  override fun moviesByGenre(genreId: Long): Flow<PagingData<VideoThumbnail>> =
    Pager(
      config = PagingConfig(
        pageSize = TmdbMoviesRemoteSource.PAGE_SIZE,
        enablePlaceholders = false,
      ),
      pagingSourceFactory = {
        MoviesByGenrePagingSource(
          tmdbMoviesRemoteSource = tmdbMoviesRemoteSource,
          genreId = genreId,
        )
      },
    ).flow

  override suspend fun credits(movieId: Int): Result<List<Person>, GeneralError> =
    tmdbMoviesRemoteSource.credits(movieId)

  override suspend fun getSimilar(movieId: Int): Result<List<VideoThumbnail>, GeneralError> =
    tmdbMoviesRemoteSource.getSimilar(movieId)

  override suspend fun getCollection(collectionId: Int): Result<MovieCollection, GeneralError> =
    tmdbMoviesRemoteSource.getCollection(collectionId)
}
