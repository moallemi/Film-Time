package io.filmtime.tv.ui.similar

import io.filmtime.data.model.VideoType

internal sealed interface SimilarAction {
  data class LoadSimilar(val videoId: Int, val videoType: VideoType) : SimilarAction
}
