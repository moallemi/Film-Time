package io.filmtime.domain.testing

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Result
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.domain.tmdb.shows.GetTrendingShowsUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeGetTrendingShowsUseCase : GetTrendingShowsUseCase {
  private var showsResult: Result<List<VideoThumbnail>, GeneralError>? = null

  fun setResult(result: Result<List<VideoThumbnail>, GeneralError>) {
    showsResult = result
  }

  override suspend fun invoke(): Flow<Result<List<VideoThumbnail>, GeneralError>> {
    return flowOf(showsResult ?: Result.Success(emptyList()))
  }
}
