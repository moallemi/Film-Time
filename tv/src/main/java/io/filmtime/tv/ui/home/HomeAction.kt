package io.filmtime.tv.ui.home

internal sealed interface HomeAction {
  data object Reload : HomeAction
}
