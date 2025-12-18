package io.filmtime.domain.testing.util

import io.filmtime.data.model.VideoId
import io.filmtime.data.model.VideoThumbnail
import io.filmtime.data.model.VideoType

object TestDataFactory {

  fun createVideoThumbnail(
    id: Int,
    title: String = "Test Video $id",
    type: VideoType = VideoType.Movie,
    year: Int = 2024,
    posterUrl: String = "https://example.com/poster_$id.jpg",
  ) = VideoThumbnail(
    ids = VideoId(traktId = id, tmdbId = id),
    title = title,
    posterUrl = posterUrl,
    year = year,
    type = type,
  )

  fun createMovies(count: Int): List<VideoThumbnail> =
    (1..count).map { createVideoThumbnail(id = it, title = "Movie $it", type = VideoType.Movie) }

  fun createShows(count: Int): List<VideoThumbnail> =
    (1..count).map { createVideoThumbnail(id = it, title = "Show $it", type = VideoType.Show) }
}
