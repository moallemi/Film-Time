package io.filmtime.domain.tmdb.search.impl

import androidx.paging.PagingData
import io.filmtime.data.model.SearchResult
import io.filmtime.data.model.SearchType
import io.filmtime.data.tmdb.search.TmdbSearchRepository
import io.filmtime.domain.tmdb.search.SearchTmdbUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class SearchTmdbUseCaseImpl @Inject constructor(
  private val tmdbSearchRepository: TmdbSearchRepository,
) : SearchTmdbUseCase {

  override fun invoke(
    query: String,
    type: SearchType,
  ): Flow<PagingData<SearchResult>> =
    tmdbSearchRepository.streamSearch(type, query)
}
