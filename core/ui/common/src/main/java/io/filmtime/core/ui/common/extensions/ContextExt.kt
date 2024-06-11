package io.filmtime.core.ui.common.extensions

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import io.filmtime.core.ui.common.R

fun Context.safeStartActivity(intent: Intent) {
  try {
    startActivity(intent)
  } catch (e: Exception) {
    Log.e(javaClass.simpleName, e.localizedMessage ?: e.stackTraceToString())

    Toast.makeText(
      this,
      getString(R.string.core_ui_device_does_not_support_this_action),
      Toast.LENGTH_LONG,
    ).show()
  }
}
