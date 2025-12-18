package io.filmtime.domain.testing

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoListType
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.domain.tmdb.movies.GetMoviesListUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetMoviesListUseCase : GetMoviesListUseCase {
  private val moviesResults = mutableMapOf<VideoListType, Result<List<VideoThumbnail>, GeneralError>>()

  fun setResult(videoListType: VideoListType, result: Result<List<VideoThumbnail>, GeneralError>) {
    moviesResults[videoListType] = result
  }

  override suspend fun invoke(videoListType: VideoListType): Flow<Result<List<VideoThumbnail>, GeneralError>> {
    return flowOf(moviesResults[videoListType] ?: Result.Success(emptyList()))
  }
}
