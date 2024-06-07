package io.filmtime.domain.bookmarks

import io.filmtime.data.model.VideoType

interface DeleteBookmarkUseCase {
  suspend operator fun invoke(tmdbId: Int, type: VideoType)
}
