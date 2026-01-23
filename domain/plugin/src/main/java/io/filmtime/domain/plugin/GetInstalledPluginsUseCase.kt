package io.filmtime.domain.plugin

import io.filmtime.core.plugin.api.PluginMetadata
import kotlinx.coroutines.flow.Flow

interface GetInstalledPluginsUseCase {
  operator fun invoke(): Flow<List<PluginMetadata>>
}
