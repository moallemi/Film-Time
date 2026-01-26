package io.filmtime.domain.plugin

import io.filmtime.core.plugin.api.PluginError
import io.filmtime.data.model.Result

interface LogoutPluginUseCase {
  suspend operator fun invoke(pluginId: String): Result<Boolean, PluginError>
}
