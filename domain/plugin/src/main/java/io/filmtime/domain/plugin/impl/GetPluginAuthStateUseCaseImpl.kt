package io.filmtime.domain.plugin.impl

import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.data.model.Result
import io.filmtime.data.plugin.discovery.PluginDiscoveryRepository
import io.filmtime.domain.plugin.GetPluginAuthStateUseCase
import javax.inject.Inject

internal class GetPluginAuthStateUseCaseImpl @Inject constructor(
  private val pluginDiscoveryRepository: PluginDiscoveryRepository,
) : GetPluginAuthStateUseCase {

  override suspend fun invoke(pluginId: String): Result<PluginAuthState, PluginError> =
    pluginDiscoveryRepository.getPluginAuthState(pluginId)
}
