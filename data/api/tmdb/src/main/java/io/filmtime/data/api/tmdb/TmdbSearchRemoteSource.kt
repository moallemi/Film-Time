package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchResult.TvShow
import io.filmtime.data.model.SearchResult.Video

interface TmdbSearchRemoteSource {

  suspend fun searchMovies(page: Int, query: String): Result<List<Video>, GeneralError>

  suspend fun searchTvShows(page: Int, query: String): Result<List<TvShow>, GeneralError>

  suspend fun searchAll(page: Int, query: String): Result<List<SearchResult>, GeneralError>

  companion object {
    const val PAGE_SIZE = 20 // TMDB API default page size
  }
}
