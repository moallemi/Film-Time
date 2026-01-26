package io.filmtime.domain.plugin

import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.data.model.Result

interface GetPluginAuthStateUseCase {
  suspend operator fun invoke(pluginId: String): Result<PluginAuthState, PluginError>
}
