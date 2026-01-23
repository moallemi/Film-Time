package io.filmtime.domain.plugin

import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.StreamRequest
import io.filmtime.core.plugin.api.StreamResponse
import io.filmtime.data.model.Result

interface GetStreamFromPluginUseCase {
  suspend operator fun invoke(
    pluginId: String,
    request: StreamRequest,
  ): Result<StreamResponse, PluginError>
}
