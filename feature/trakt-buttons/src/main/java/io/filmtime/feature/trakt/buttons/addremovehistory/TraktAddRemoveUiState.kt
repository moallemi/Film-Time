package io.filmtime.feature.trakt.buttons.addremovehistory

internal data class TraktAddRemoveUiState(
  val isLoading: Boolean = true,
  val isWatched: Boolean = false,
  val isError: Boolean = false,
  val traktId: Int? = null,
)
