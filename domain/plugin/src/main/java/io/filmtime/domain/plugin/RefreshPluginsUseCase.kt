package io.filmtime.domain.plugin

interface RefreshPluginsUseCase {
  suspend operator fun invoke()
}
