package io.filmtime.feature.movie.detail

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.Person

data class MovieDetailCreditState(
  val isLoading: Boolean = false,
  val credit: List<Person> = emptyList(),
  val error: UiMessage? = null,
)
