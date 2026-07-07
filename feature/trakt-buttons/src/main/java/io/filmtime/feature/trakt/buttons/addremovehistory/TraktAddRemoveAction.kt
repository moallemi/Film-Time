package io.filmtime.feature.trakt.buttons.addremovehistory

import io.filmtime.data.model.VideoType

internal sealed interface TraktAddRemoveAction {
  data class CheckIfWatched(val videoType: VideoType, val tmdbId: Int) : TraktAddRemoveAction
  data object AddToHistory : TraktAddRemoveAction
  data object RemoveFromHistory : TraktAddRemoveAction
}
