package io.filmtime.feature.cast.detail

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.PersonDetail

data class CastDetailState(
  val isLoading: Boolean = false,
  val person: PersonDetail? = null,
  val message: String? = null,
  val error: UiMessage? = null,
)
