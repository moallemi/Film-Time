package io.filmtime.feature.home

internal sealed interface HomeAction {
  data object Reload : HomeAction
}
