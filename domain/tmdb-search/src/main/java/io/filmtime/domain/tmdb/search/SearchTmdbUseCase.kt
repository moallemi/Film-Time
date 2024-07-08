package io.filmtime.domain.tmdb.search

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType

interface SearchTmdbUseCase {
  suspend operator fun invoke(query: String, type: SearchType): Result<List<SearchResult>, GeneralError>
}
