package io.filmtime.domain.testing

import io.filmtime.core.plugin.api.PluginError
import io.filmtime.data.model.Result
import io.filmtime.domain.plugin.LogoutPluginUseCase

class FakeLogoutPluginUseCase : LogoutPluginUseCase {

  private var result: Result<Boolean, PluginError> = Result.Success(true)

  var lastPluginId: String? = null
    private set

  fun setResult(result: Result<Boolean, PluginError>) {
    this.result = result
  }

  override suspend fun invoke(pluginId: String): Result<Boolean, PluginError> {
    lastPluginId = pluginId
    return result
  }
}
