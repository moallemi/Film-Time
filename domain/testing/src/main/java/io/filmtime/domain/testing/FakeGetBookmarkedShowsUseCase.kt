package io.filmtime.domain.testing

import io.filmtime.data.model.VideoThumbnail
import io.filmtime.domain.tmdb.shows.GetBookmarkedShowsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGetBookmarkedShowsUseCase : GetBookmarkedShowsUseCase {
  private val bookmarkedShowsFlow = MutableStateFlow<List<VideoThumbnail>>(emptyList())

  fun setBookmarkedShows(shows: List<VideoThumbnail>) {
    bookmarkedShowsFlow.value = shows
  }

  override suspend fun invoke(): Flow<List<VideoThumbnail>> = bookmarkedShowsFlow
}
