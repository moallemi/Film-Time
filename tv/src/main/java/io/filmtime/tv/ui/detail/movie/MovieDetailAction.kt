package io.filmtime.tv.ui.detail.movie

internal sealed interface MovieDetailAction {
  data object Play : MovieDetailAction
  data object AddBookmark : MovieDetailAction
  data object RemoveBookmark : MovieDetailAction
}
