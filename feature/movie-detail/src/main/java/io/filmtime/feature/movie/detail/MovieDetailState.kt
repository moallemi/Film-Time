package io.filmtime.feature.movie.detail

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.MovieCollection
import io.filmtime.data.model.Ratings
import io.filmtime.data.model.StreamInfo
import io.filmtime.data.model.VideoDetail

data class MovieDetailState(
  val isLoading: Boolean = false,
  val isBookmarked: Boolean = false,
  val hasCollection: Boolean = false,
  val isCollectionLoading: Boolean = false,
  val collection: MovieCollection? = null,
  val videoDetail: VideoDetail? = null,
  val ratings: Ratings? = null,
  val isStreamLoading: Boolean = false,
  val streamInfo: StreamInfo? = null,
  val error: UiMessage? = null,
)
