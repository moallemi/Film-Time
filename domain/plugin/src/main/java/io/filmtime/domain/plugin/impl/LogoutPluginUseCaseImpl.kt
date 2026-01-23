package io.filmtime.domain.plugin.impl

import io.filmtime.core.plugin.api.PluginError
import io.filmtime.data.model.Result
import io.filmtime.data.plugin.discovery.PluginDiscoveryRepository
import io.filmtime.domain.plugin.LogoutPluginUseCase
import javax.inject.Inject

internal class LogoutPluginUseCaseImpl @Inject constructor(
  private val pluginDiscoveryRepository: PluginDiscoveryRepository,
) : LogoutPluginUseCase {

  override suspend fun invoke(pluginId: String): Result<Boolean, PluginError> =
    pluginDiscoveryRepository.logoutPlugin(pluginId)
}
