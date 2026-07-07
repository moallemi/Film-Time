package io.filmtime.feature.similar

import io.filmtime.data.model.VideoType

internal sealed interface SimilarAction {
  data class LoadSimilar(val videoId: Int, val videoType: VideoType) : SimilarAction
}
