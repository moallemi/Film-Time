package io.filmtime.domain.plugin.impl

import android.content.Intent
import io.filmtime.core.plugin.api.PluginContract
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.domain.plugin.CreatePluginLoginIntentUseCase
import javax.inject.Inject

internal class CreatePluginLoginIntentUseCaseImpl @Inject constructor() : CreatePluginLoginIntentUseCase {

  override fun invoke(plugin: PluginMetadata): Intent? {
    val loginActivity = plugin.loginActivityClass ?: return null
    return Intent(PluginContract.Auth.ACTION_LOGIN).apply {
      setClassName(plugin.packageName, loginActivity)
    }
  }
}
