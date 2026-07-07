package io.filmtime.tv.ui.detail.movie

internal sealed interface MovieDetailNavigationEvent {
  data class NavigateToPlayer(val url: String) : MovieDetailNavigationEvent
}
