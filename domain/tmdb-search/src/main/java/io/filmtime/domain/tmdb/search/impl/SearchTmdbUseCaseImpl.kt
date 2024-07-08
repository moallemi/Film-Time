package io.filmtime.domain.tmdb.search.impl

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType
import io.filmtime.data.model.SearchType.All
import io.filmtime.data.model.SearchType.Movie
import io.filmtime.data.model.SearchType.Show
import io.filmtime.data.tmdb.search.TmdbSearchRepository
import io.filmtime.domain.tmdb.search.SearchTmdbUseCase
import javax.inject.Inject

internal class SearchTmdbUseCaseImpl @Inject constructor(
  private val tmdbSearchRepository: TmdbSearchRepository,
) : SearchTmdbUseCase {

  override suspend fun invoke(
    query: String,
    type: SearchType,
  ): Result<List<SearchResult>, GeneralError> = when (type) {
    All -> tmdbSearchRepository.searchAll(query)
    Movie -> tmdbSearchRepository.searchMovies(query)
    Show -> tmdbSearchRepository.searchTvShows(query)
  }
}
