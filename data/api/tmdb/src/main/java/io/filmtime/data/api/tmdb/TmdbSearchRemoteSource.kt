package io.filmtime.data.api.tmdb

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchResult.TvShow
import io.filmtime.data.model.SearchResult.Video

interface TmdbSearchRemoteSource {

  suspend fun searchMovies(query: String): Result<List<Video>, GeneralError>

  suspend fun searchTvShows(query: String): Result<List<TvShow>, GeneralError>

  suspend fun searchAll(query: String): Result<List<SearchResult>, GeneralError>
}
