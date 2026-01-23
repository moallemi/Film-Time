package io.filmtime.feature.plugin.manager

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PluginPreferencesImpl @Inject constructor(
  @ApplicationContext private val context: Context,
) : PluginPreferences {

  private val prefs by lazy {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
  }

  override fun getDefaultPluginId(): String? {
    return prefs.getString(KEY_DEFAULT_PLUGIN_ID, null)
  }

  override fun setDefaultPluginId(pluginId: String?) {
    prefs.edit().putString(KEY_DEFAULT_PLUGIN_ID, pluginId).apply()
  }

  companion object {
    private const val PREFS_NAME = "plugin_preferences"
    private const val KEY_DEFAULT_PLUGIN_ID = "default_plugin_id"
  }
}
