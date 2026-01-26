package io.filmtime.plugin.sample

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import androidx.core.content.edit
import org.json.JSONArray
import org.json.JSONObject

class SamplePluginProvider : ContentProvider() {

  companion object {
    private const val AUTHORITY = "io.filmtime.plugin.sample"
    private const val PREFS_NAME = "plugin_auth"
    private const val KEY_TOKEN = "auth_token"
    private const val KEY_USERNAME = "username"

    private const val CODE_METADATA = 1
    private const val CODE_STREAM_MOVIE = 2
    private const val CODE_STREAM_SHOW = 3
  }

  private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
    addURI(AUTHORITY, PluginContract.PATH_METADATA, CODE_METADATA)
    addURI(AUTHORITY, "${PluginContract.PATH_STREAM}/${PluginContract.PATH_MOVIE}", CODE_STREAM_MOVIE)
    addURI(AUTHORITY, "${PluginContract.PATH_STREAM}/${PluginContract.PATH_SHOW}", CODE_STREAM_SHOW)
  }

  override fun onCreate(): Boolean = true

  override fun query(
    uri: Uri,
    projection: Array<out String>?,
    selection: String?,
    selectionArgs: Array<out String>?,
    sortOrder: String?,
  ): Cursor? {
    return when (uriMatcher.match(uri)) {
      CODE_METADATA -> queryMetadata()
      CODE_STREAM_MOVIE -> queryStream(uri)
      CODE_STREAM_SHOW -> queryStream(uri)
      else -> null
    }
  }

  override fun call(method: String, arg: String?, extras: Bundle?): Bundle? {
    return when (method) {
      PluginContract.Auth.METHOD_GET_AUTH_STATE -> getAuthState()
      PluginContract.Auth.METHOD_LOGOUT -> logout()
      else -> null
    }
  }

  private fun queryMetadata(): Cursor {
    val cursor = MatrixCursor(
      arrayOf(
        PluginContract.Metadata.COLUMN_PLUGIN_ID,
        PluginContract.Metadata.COLUMN_NAME,
        PluginContract.Metadata.COLUMN_DESCRIPTION,
        PluginContract.Metadata.COLUMN_VERSION,
        PluginContract.Metadata.COLUMN_ICON_URL,
        PluginContract.Metadata.COLUMN_REQUIRES_AUTH,
        PluginContract.Metadata.COLUMN_LOGIN_ACTIVITY,
      ),
    )
    cursor.addRow(
      arrayOf<Any>(
        "sample",
        "Sample Stream with Auth Plugin",
        "A sample plugin for testing the FilmTime plugin system",
        "1.0.0",
        "",
        1, // requires_auth = true
        "io.filmtime.plugin.sample.LoginActivity",
      ),
    )
    return cursor
  }

  private fun queryStream(uri: Uri): Cursor {
    val cursor = MatrixCursor(
      arrayOf(
        PluginContract.Stream.COLUMN_STREAM_URL,
        PluginContract.Stream.COLUMN_QUALITY,
        PluginContract.Stream.COLUMN_STREAM_TYPE,
        PluginContract.Stream.COLUMN_TITLE,
        PluginContract.Stream.COLUMN_HEADERS,
        PluginContract.Stream.COLUMN_SUBTITLES,
      ),
    )

    val context = context ?: return cursor

    // Check authentication
    if (!isAuthenticated(context)) {
      val extras = Bundle().apply {
        putBoolean(PluginContract.Stream.EXTRA_AUTH_REQUIRED, true)
      }
      cursor.extras = extras
      return cursor
    }

    val token = getToken(context) ?: ""
    val headers = JSONObject().apply {
      put("X-Plugin-Token", token)
      put("User-Agent", "FilmTime-SamplePlugin/1.0")
    }.toString()

    val subtitles = JSONArray().apply {
      put(
        JSONObject().apply {
          put(
            "url",
            "https://raw.githubusercontent.com/nicholasgasior/gopher-srt/master/test-data/SampleSubtitle.srt",
          )
          put("language", "en")
          put("label", "English (Sample)")
        },
      )
    }.toString()

    // HLS stream (primary)
    cursor.addRow(
      arrayOf(
        "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
        PluginContract.Quality.HD,
        PluginContract.StreamType.HLS,
        "Big Buck Bunny (HLS)",
        headers,
        subtitles,
      ),
    )

    // DASH stream (secondary)
    cursor.addRow(
      arrayOf(
        "https://dash.akamaized.net/akamai/bbb_30fps/bbb_30fps.mpd",
        PluginContract.Quality.FHD,
        PluginContract.StreamType.DASH,
        "Big Buck Bunny (DASH)",
        headers,
        "",
      ),
    )

    // MP4 stream (fallback)
    cursor.addRow(
      arrayOf(
        "https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4",
        PluginContract.Quality.HD,
        PluginContract.StreamType.MP4,
        "Big Buck Bunny (MP4)",
        headers,
        "",
      ),
    )

    return cursor
  }

  private fun getAuthState(): Bundle {
    val context = context ?: return Bundle()
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val isAuthenticated = prefs.getString(KEY_TOKEN, null) != null

    return Bundle().apply {
      putBoolean(PluginContract.Auth.KEY_REQUIRES_AUTH, true)
      putBoolean(PluginContract.Auth.KEY_IS_AUTHENTICATED, isAuthenticated)
      if (isAuthenticated) {
        putString(
          PluginContract.Auth.KEY_AUTH_DISPLAY_NAME,
          prefs.getString(KEY_USERNAME, "Sample User"),
        )
      }
    }
  }

  private fun logout(): Bundle {
    val context = context ?: return Bundle().apply {
      putBoolean(PluginContract.Auth.KEY_LOGOUT_SUCCESS, false)
    }
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
      .edit {
        clear()
      }

    return Bundle().apply {
      putBoolean(PluginContract.Auth.KEY_LOGOUT_SUCCESS, true)
    }
  }

  private fun isAuthenticated(context: Context): Boolean {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString(KEY_TOKEN, null) != null
  }

  private fun getToken(context: Context): String? {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    return prefs.getString(KEY_TOKEN, null)
  }

  override fun getType(uri: Uri): String? = null
  override fun insert(uri: Uri, values: ContentValues?): Uri? = null
  override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int = 0
  override fun update(
    uri: Uri,
    values: ContentValues?,
    selection: String?,
    selectionArgs: Array<out String>?,
  ): Int = 0
}
