package io.filmtime.domain.plugin.impl

import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.StreamRequest
import io.filmtime.core.plugin.api.StreamResponse
import io.filmtime.data.model.Result
import io.filmtime.data.plugin.discovery.PluginDiscoveryRepository
import io.filmtime.domain.plugin.GetStreamFromPluginUseCase
import javax.inject.Inject

internal class GetStreamFromPluginUseCaseImpl @Inject constructor(
  private val pluginDiscoveryRepository: PluginDiscoveryRepository,
) : GetStreamFromPluginUseCase {

  override suspend fun invoke(
    pluginId: String,
    request: StreamRequest,
  ): Result<StreamResponse, PluginError> =
    pluginDiscoveryRepository.getStreamFromPlugin(pluginId, request)
}
