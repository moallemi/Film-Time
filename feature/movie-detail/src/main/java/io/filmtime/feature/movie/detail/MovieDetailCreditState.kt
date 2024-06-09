package io.filmtime.feature.movie.detail

import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.Person

data class MovieDetailCreditState(
  val isLoading: Boolean = false,
  val credit: List<Person> = emptyList(),
  val message: String? = null,
  val error: GeneralError? = null,
)
