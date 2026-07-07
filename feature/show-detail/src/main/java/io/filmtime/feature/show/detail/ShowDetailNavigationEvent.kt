package io.filmtime.feature.show.detail

import io.filmtime.data.model.StreamInfo

internal sealed interface ShowDetailNavigationEvent {
  data class NavigateToPlayer(val streamInfo: StreamInfo) : ShowDetailNavigationEvent
}
