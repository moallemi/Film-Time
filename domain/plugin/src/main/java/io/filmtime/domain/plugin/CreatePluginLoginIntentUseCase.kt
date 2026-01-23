package io.filmtime.domain.plugin

import android.content.Intent
import io.filmtime.core.plugin.api.PluginMetadata

interface CreatePluginLoginIntentUseCase {
  operator fun invoke(plugin: PluginMetadata): Intent?
}
