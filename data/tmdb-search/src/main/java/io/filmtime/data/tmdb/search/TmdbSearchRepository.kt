package io.filmtime.data.tmdb.search

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchResult.TvShow

interface TmdbSearchRepository {
  suspend fun searchMovies(query: String): Result<List<SearchResult.Video>, GeneralError>

  suspend fun searchTvShows(query: String): Result<List<TvShow>, GeneralError>

  suspend fun searchAll(query: String): Result<List<SearchResult>, GeneralError>
}
