package io.filmtime.core.browser

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import io.filmtime.core.ui.common.extensions.safeStartActivity

fun Context?.openUrl(url: String, isExternal: Boolean) {
  if (this == null) {
    return
  }
  if (isExternal || customTabsPackages.isEmpty()) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    safeStartActivity(intent)
  } else {
    val uri = Uri.parse(url)
    val customTabsIntent = CustomTabsIntent.Builder()
      .setShareState(CustomTabsIntent.SHARE_STATE_OFF)
      .setBookmarksButtonEnabled(false)
      .setDownloadButtonEnabled(false)
      .setShowTitle(true)
      .build()
    customTabsIntent.intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
    customTabsIntent.launchUrl(this, uri)
  }
}

private val Context?.customTabsPackages: List<String>
  get() {
    val pm = this?.packageManager ?: return emptyList()
    // Get default VIEW intent handler.
    val activityIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))

    // Get all apps that can handle VIEW intents.
    val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
    val packagesSupportingCustomTabs = mutableListOf<String>()
    for (info in resolvedActivityList) {
      val serviceIntent = Intent()
      serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
      serviceIntent.setPackage(info.activityInfo.packageName)
      // Check if this package also resolves the Custom Tabs service.
      if (pm.resolveService(serviceIntent, 0) != null) {
        packagesSupportingCustomTabs.add(info.activityInfo.packageName)
      }
    }
    return packagesSupportingCustomTabs
  }
