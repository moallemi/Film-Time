package io.filmtime.domain.bookmarks

import io.filmtime.data.model.VideoType
import kotlinx.coroutines.flow.Flow

interface ObserveBookmarkUseCase {
  suspend operator fun invoke(tmdbId: Int, type: VideoType): Flow<Boolean>
}
