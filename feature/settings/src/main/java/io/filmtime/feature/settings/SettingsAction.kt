package io.filmtime.feature.settings

internal sealed interface SettingsAction {
  data object TraktLogout : SettingsAction
}
