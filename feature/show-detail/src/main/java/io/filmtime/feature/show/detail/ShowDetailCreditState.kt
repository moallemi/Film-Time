package io.filmtime.feature.show.detail

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person

data class ShowDetailCreditState(
  val isLoading: Boolean = false,
  val credit: List<Person> = emptyList(),
  val message: String? = null,
  val error: GeneralError? = null,
)
