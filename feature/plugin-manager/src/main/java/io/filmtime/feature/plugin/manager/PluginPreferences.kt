package io.filmtime.feature.plugin.manager

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PluginPreferences @Inject constructor(
  @ApplicationContext private val context: Context,
) {

  private val prefs by lazy {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }

  fun getDefaultPluginId(): String? {
    return prefs.getString(KEY_DEFAULT_PLUGIN_ID, null)
  }

  fun setDefaultPluginId(pluginId: String?) {
    prefs.edit().putString(KEY_DEFAULT_PLUGIN_ID, pluginId).apply()
  }

  companion object {
    private const val PREFS_NAME = "plugin_preferences"
    private const val KEY_DEFAULT_PLUGIN_ID = "default_plugin_id"
  }
}
