package io.filmtime.plugin.embed

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * Empty service used for plugin discovery via queryIntentServices.
 * The host app discovers plugins by querying for services with the STREAM_PROVIDER action.
 */
class PluginDiscoveryService : Service() {

  override fun onBind(intent: Intent?): IBinder? = null
}
