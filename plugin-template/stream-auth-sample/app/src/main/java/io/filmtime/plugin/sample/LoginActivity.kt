package io.filmtime.plugin.sample

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit

class LoginActivity : Activity() {

  companion object {
    private const val PREFS_NAME = "plugin_auth"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USERNAME = "username"
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

    findViewById<TextView>(R.id.title).text = getString(R.string.login_title)
    findViewById<TextView>(R.id.description).text = getString(R.string.login_description)

    findViewById<Button>(R.id.btn_sign_in).setOnClickListener {
      // Store a fake token
      getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit {
          putString(KEY_TOKEN, "sample_token_${System.currentTimeMillis()}")
            .putString(KEY_USERNAME, "Sample User")
        }

      val resultIntent = Intent().apply {
        putExtra(PluginContract.Auth.EXTRA_LOGIN_RESULT, PluginContract.Auth.LOGIN_RESULT_SUCCESS)
      }
      setResult(RESULT_OK, resultIntent)
      finish()
    }

    findViewById<Button>(R.id.btn_cancel).setOnClickListener {
      val resultIntent = Intent().apply {
        putExtra(PluginContract.Auth.EXTRA_LOGIN_RESULT, PluginContract.Auth.LOGIN_RESULT_CANCELLED)
      }
      setResult(RESULT_CANCELED, resultIntent)
      finish()
    }
  }
}
