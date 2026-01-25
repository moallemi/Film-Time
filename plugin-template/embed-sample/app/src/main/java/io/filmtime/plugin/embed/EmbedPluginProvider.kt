package io.filmtime.plugin.embed

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import android.os.Bundle

/**
 * An embed plugin provider that returns URLs to be opened in a WebView.
 * This demonstrates how to create plugins that provide embed/iframe URLs
 * instead of direct stream URLs.
 *
 * Example services that work this way:
 * - https://foo.com/embed/movie/{tmdb_id}
 * - Similar embed providers
 */
class EmbedPluginProvider : ContentProvider() {

  companion object {
    private const val AUTHORITY = "io.filmtime.plugin.embed"
    private const val BASE_URL = "https://foo.com"

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
      CODE_STREAM_MOVIE -> queryMovieEmbed(uri)
      CODE_STREAM_SHOW -> queryShowEmbed(uri)
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
        "embed",
        "Embed Plugin",
        "A plugin that provides embed URLs to be opened in a WebView",
        "1.0.0",
        "",
        0, // requires_auth = false
        "", // no login activity needed
      ),
    )
    return cursor
  }

  private fun queryMovieEmbed(uri: Uri): Cursor {
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

    val tmdbId = uri.getQueryParameter(PluginContract.Stream.PARAM_TMDB_ID)
    val imdbId = uri.getQueryParameter(PluginContract.Stream.PARAM_IMDB_ID)

    // Prefer IMDB ID if available (with tt prefix), otherwise use TMDB ID
    val id = imdbId ?: tmdbId ?: return cursor

    val embedUrl = "$BASE_URL/embed/movie/$id"

    cursor.addRow(
      arrayOf(
        embedUrl,
        PluginContract.Quality.AUTO,
        PluginContract.StreamType.EMBED, // This tells the app to open in WebView
        "Watch Movie",
        "", // No headers needed for embed
        "", // No subtitles - handled by the embed player
      ),
    )

    return cursor
  }

  private fun queryShowEmbed(uri: Uri): Cursor {
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

    val tmdbId = uri.getQueryParameter(PluginContract.Stream.PARAM_TMDB_ID)
    val imdbId = uri.getQueryParameter(PluginContract.Stream.PARAM_IMDB_ID)
    val season = uri.getQueryParameter(PluginContract.Stream.PARAM_SEASON)
    val episode = uri.getQueryParameter(PluginContract.Stream.PARAM_EPISODE)

    // Prefer IMDB ID if available, otherwise use TMDB ID
    val id = imdbId ?: tmdbId ?: return cursor

    if (season == null || episode == null) return cursor

    val embedUrl = "$BASE_URL/embed/tv/$id/$season/$episode"

    cursor.addRow(
      arrayOf(
        embedUrl,
        PluginContract.Quality.AUTO,
        PluginContract.StreamType.EMBED, // This tells the app to open in WebView
        "Watch S${season}E$episode",
        "", // No headers needed for embed
        "", // No subtitles - handled by the embed player
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
