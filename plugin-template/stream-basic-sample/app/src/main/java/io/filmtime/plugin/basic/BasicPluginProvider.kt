package io.filmtime.plugin.basic

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle
import org.json.JSONObject

/**
 * A basic plugin provider that does not require authentication.
 * This is the simplest form of a FilmTime stream plugin.
 */
class BasicPluginProvider : ContentProvider() {

  companion object {
    private const val AUTHORITY = "io.filmtime.plugin.basic"

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
        "basic",
        "Basic Stream Plugin",
        "A basic plugin that provides streams without authentication",
        "1.0.0",
        "",
        0, // requires_auth = false
        "", // no login activity needed
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

    val headers = JSONObject().apply {
      put("User-Agent", "FilmTime-BasicPlugin/1.0")
    }.toString()

    // HLS stream (primary)
    cursor.addRow(
      arrayOf(
        "https://test-streams.mux.dev/x36xhzz/x36xhzz.m3u8",
        PluginContract.Quality.HD,
        PluginContract.StreamType.HLS,
        "Big Buck Bunny (HLS)",
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
    return Bundle().apply {
      putBoolean(PluginContract.Auth.KEY_REQUIRES_AUTH, false)
    }
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
