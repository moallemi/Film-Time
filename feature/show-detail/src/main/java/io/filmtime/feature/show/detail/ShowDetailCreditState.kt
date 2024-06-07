package io.filmtime.feature.show.detail

import io.filmtime.data.model.CreditItem
import io.filmtime.data.model.GeneralError

data class ShowDetailCreditState(
  val isLoading: Boolean = false,
  val credit: List<CreditItem> = emptyList(),
  val message: String? = null,
  val error: GeneralError? = null,
)
