package io.filmtime.domain.testing

import android.content.Intent
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.domain.plugin.CreatePluginLoginIntentUseCase

class FakeCreatePluginLoginIntentUseCase : CreatePluginLoginIntentUseCase {

  var lastPlugin: PluginMetadata? = null
    private set

  private var intent: Intent? = Intent()

  fun setIntent(intent: Intent?) {
    this.intent = intent
  }

  override fun invoke(plugin: PluginMetadata): Intent? {
    lastPlugin = plugin
    return intent
  }
}
