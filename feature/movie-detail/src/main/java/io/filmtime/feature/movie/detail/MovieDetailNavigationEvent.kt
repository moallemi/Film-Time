package io.filmtime.feature.movie.detail

import io.filmtime.data.model.StreamInfo

internal sealed interface MovieDetailNavigationEvent {
  data class NavigateToPlayer(val streamInfo: StreamInfo) : MovieDetailNavigationEvent
}
