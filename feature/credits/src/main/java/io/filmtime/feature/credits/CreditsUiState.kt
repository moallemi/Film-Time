package io.filmtime.feature.credits

import io.filmtime.core.ui.common.UiMessage
import io.filmtime.data.model.Person

data class CreditsUiState(
  val isLoading: Boolean = false,
  val credit: List<Person> = emptyList(),
  val error: UiMessage? = null,
)
