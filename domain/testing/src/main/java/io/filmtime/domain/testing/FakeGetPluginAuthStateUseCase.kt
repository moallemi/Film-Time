package io.filmtime.domain.testing

import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.data.model.Result
import io.filmtime.domain.plugin.GetPluginAuthStateUseCase

class FakeGetPluginAuthStateUseCase : GetPluginAuthStateUseCase {

  private val authStates = mutableMapOf<String, Result<PluginAuthState, PluginError>>()

  fun setAuthState(pluginId: String, result: Result<PluginAuthState, PluginError>) {
    authStates[pluginId] = result
  }

  override suspend fun invoke(pluginId: String): Result<PluginAuthState, PluginError> =
    authStates[pluginId] ?: Result.Success(PluginAuthState.NotRequired)
}
