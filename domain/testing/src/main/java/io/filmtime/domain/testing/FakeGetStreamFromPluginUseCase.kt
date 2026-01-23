package io.filmtime.domain.testing

import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.StreamRequest
import io.filmtime.core.plugin.api.StreamResponse
import io.filmtime.data.model.Result
import io.filmtime.domain.plugin.GetStreamFromPluginUseCase

class FakeGetStreamFromPluginUseCase : GetStreamFromPluginUseCase {

  private var result: Result<StreamResponse, PluginError> =
    Result.Failure(PluginError.NoStreamsAvailable)

  fun setResult(result: Result<StreamResponse, PluginError>) {
    this.result = result
  }

  override suspend fun invoke(
    pluginId: String,
    request: StreamRequest,
  ): Result<StreamResponse, PluginError> = result
}
