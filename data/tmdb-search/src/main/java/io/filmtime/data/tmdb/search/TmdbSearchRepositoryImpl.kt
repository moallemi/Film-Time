package io.filmtime.data.tmdb.search

import io.filmtime.data.api.tmdb.TmdbSearchRemoteSource
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchResult.TvShow
import io.filmtime.data.model.SearchResult.Video
import javax.inject.Inject

internal class TmdbSearchRepositoryImpl @Inject constructor(
  private val tmdbDataSource: TmdbSearchRemoteSource,
) : TmdbSearchRepository {
  override suspend fun searchMovies(query: String): Result<List<Video>, GeneralError> =
    tmdbDataSource.searchMovies(query)

  override suspend fun searchTvShows(query: String): Result<List<TvShow>, GeneralError> =
    tmdbDataSource.searchTvShows(query)

  override suspend fun searchAll(query: String): Result<List<SearchResult>, GeneralError> =
    tmdbDataSource.searchAll(query)
}
