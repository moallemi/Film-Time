package io.filmtime.domain.testing

import io.filmtime.data.model.VideoThumbnail
import io.filmtime.domain.tmdb.movies.GetBookmarkedMoviesUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

class FakeGetBookmarkedMoviesUseCase : GetBookmarkedMoviesUseCase {
  private val bookmarkedMoviesFlow = MutableStateFlow<List<VideoThumbnail>>(emptyList())

  fun setBookmarkedMovies(movies: List<VideoThumbnail>) {
    bookmarkedMoviesFlow.value = movies
  }

  override suspend fun invoke(): Flow<List<VideoThumbnail>> = bookmarkedMoviesFlow
}
