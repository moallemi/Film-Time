package io.filmtime.domain.testing

import io.filmtime.domain.plugin.RefreshPluginsUseCase

class FakeRefreshPluginsUseCase : RefreshPluginsUseCase {

  var refreshCount = 0
    private set

  override suspend fun invoke() {
    refreshCount++
  }
}
