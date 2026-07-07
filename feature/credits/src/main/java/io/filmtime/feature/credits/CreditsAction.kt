package io.filmtime.feature.credits

import io.filmtime.data.model.VideoType

internal sealed interface CreditsAction {
  data class LoadCredits(val videoId: Int, val videoType: VideoType) : CreditsAction
}
