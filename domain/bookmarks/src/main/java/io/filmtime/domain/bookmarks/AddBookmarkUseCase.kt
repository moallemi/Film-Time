package io.filmtime.domain.bookmarks

import io.filmtime.data.model.VideoType

interface AddBookmarkUseCase {
  suspend operator fun invoke(tmdbId: Int, type: VideoType)
}
