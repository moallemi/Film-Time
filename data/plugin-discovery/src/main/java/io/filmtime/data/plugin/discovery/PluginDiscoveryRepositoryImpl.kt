package io.filmtime.data.plugin.discovery

import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ProviderInfo
import android.content.pm.ResolveInfo
import android.net.Uri
import android.os.Build
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import io.filmtime.core.plugin.api.PluginAuthState
import io.filmtime.core.plugin.api.PluginContract
import io.filmtime.core.plugin.api.PluginError
import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.core.plugin.api.PluginStream
import io.filmtime.core.plugin.api.PluginSubtitle
import io.filmtime.core.plugin.api.StreamRequest
import io.filmtime.core.plugin.api.StreamResponse
import io.filmtime.data.model.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class PluginDiscoveryRepositoryImpl @Inject constructor(
  @ApplicationContext private val context: Context,
) : PluginDiscoveryRepository {

  private val _plugins = MutableStateFlow<List<PluginMetadata>>(emptyList())

  companion object {
    private const val TAG = "PluginDiscovery"
  }

  private val contentResolver: ContentResolver
    get() = context.contentResolver

  override fun getInstalledPlugins(): Flow<List<PluginMetadata>> {
    return _plugins.asStateFlow()
  }

  override suspend fun refreshPlugins() {
    withContext(Dispatchers.IO) {
      val plugins = discoverPlugins()
      _plugins.value = plugins
    }
  }

  private fun discoverPlugins(): List<PluginMetadata> {
    val packageManager = context.packageManager

    val pluginPackages = discoverPluginPackages(packageManager)

    return pluginPackages
      .mapNotNull { packageName -> findPluginProvider(packageManager, packageName) }
      .mapNotNull { providerInfo -> loadPluginMetadata(providerInfo) }
  }

  private fun discoverPluginPackages(packageManager: PackageManager): Set<String> {
    val intent = Intent(PluginContract.ACTION_STREAM_PROVIDER)
    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      PackageManager.MATCH_ALL
    } else {
      0
    }

    val resolveInfos: List<ResolveInfo> = packageManager.queryIntentServices(intent, flags)

    return resolveInfos
      .mapNotNull { it.serviceInfo?.packageName }
      .toSet()
  }

  private fun findPluginProvider(packageManager: PackageManager, packageName: String): ProviderInfo? {
    return try {
      val packageInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        packageManager.getPackageInfo(
          packageName,
          PackageManager.PackageInfoFlags.of(PackageManager.GET_PROVIDERS.toLong()),
        )
      } else {
        @Suppress("DEPRECATION")
        packageManager.getPackageInfo(packageName, PackageManager.GET_PROVIDERS)
      }

      packageInfo.providers?.find { provider ->
        provider.authority?.startsWith(PluginContract.AUTHORITY_PREFIX) == true
      }
    } catch (e: PackageManager.NameNotFoundException) {
      null
    }
  }

  private fun loadPluginMetadata(providerInfo: ProviderInfo): PluginMetadata? {
    val authority = providerInfo.authority ?: return null
    val metadataUri = Uri.parse("content://$authority/${PluginContract.PATH_METADATA}")

    return try {
      contentResolver.query(metadataUri, null, null, null, null)?.use { cursor ->
        if (cursor.moveToFirst()) {
          val pluginIdIndex = cursor.getColumnIndex(PluginContract.Metadata.COLUMN_PLUGIN_ID)
          val nameIndex = cursor.getColumnIndex(PluginContract.Metadata.COLUMN_NAME)
          val descriptionIndex = cursor.getColumnIndex(PluginContract.Metadata.COLUMN_DESCRIPTION)
          val versionIndex = cursor.getColumnIndex(PluginContract.Metadata.COLUMN_VERSION)
          val iconUrlIndex = cursor.getColumnIndex(PluginContract.Metadata.COLUMN_ICON_URL)
          val requiresAuthIndex = cursor.getColumnIndex(PluginContract.Metadata.COLUMN_REQUIRES_AUTH)
          val loginActivityIndex = cursor.getColumnIndex(PluginContract.Metadata.COLUMN_LOGIN_ACTIVITY)

          PluginMetadata(
            pluginId = if (pluginIdIndex >= 0) cursor.getString(pluginIdIndex) else authority,
            name = if (nameIndex >= 0) cursor.getString(nameIndex) else "Unknown Plugin",
            description = if (descriptionIndex >= 0) cursor.getString(descriptionIndex) else "",
            version = if (versionIndex >= 0) cursor.getString(versionIndex) else "1.0.0",
            iconUrl = if (iconUrlIndex >= 0) cursor.getString(iconUrlIndex) else null,
            authority = authority,
            packageName = providerInfo.packageName,
            requiresAuth = if (requiresAuthIndex >= 0) cursor.getInt(requiresAuthIndex) == 1 else false,
            loginActivityClass = if (loginActivityIndex >= 0) cursor.getString(loginActivityIndex) else null,
          )
        } else {
          null
        }
      }
    } catch (e: Exception) {
      Log.e(TAG, "Failed to load plugin metadata for authority: $authority", e)
      null
    }
  }

  override suspend fun getStreamFromPlugin(
    pluginId: String,
    request: StreamRequest,
  ): Result<StreamResponse, PluginError> = withContext(Dispatchers.IO) {
    val plugin = _plugins.value.find { it.pluginId == pluginId }
      ?: return@withContext Result.Failure(PluginError.PluginNotFound)

    val uri = buildStreamUri(plugin.authority, request)

    try {
      val streams = mutableListOf<PluginStream>()

      contentResolver.query(uri, null, null, null, null)?.use { cursor ->
        val extras = cursor.extras
        if (extras?.getBoolean(PluginContract.Stream.EXTRA_AUTH_REQUIRED, false) == true) {
          return@withContext Result.Failure(PluginError.AuthenticationRequired)
        }

        val urlIndex = cursor.getColumnIndex(PluginContract.Stream.COLUMN_STREAM_URL)
        val qualityIndex = cursor.getColumnIndex(PluginContract.Stream.COLUMN_QUALITY)
        val typeIndex = cursor.getColumnIndex(PluginContract.Stream.COLUMN_STREAM_TYPE)
        val titleIndex = cursor.getColumnIndex(PluginContract.Stream.COLUMN_TITLE)
        val headersIndex = cursor.getColumnIndex(PluginContract.Stream.COLUMN_HEADERS)
        val subtitlesIndex = cursor.getColumnIndex(PluginContract.Stream.COLUMN_SUBTITLES)

        while (cursor.moveToNext()) {
          val url = if (urlIndex >= 0) cursor.getString(urlIndex) else continue
          if (url.isNullOrEmpty() || !isValidStreamUrl(url)) continue

          val stream = PluginStream(
            url = url,
            quality = if (qualityIndex >= 0) cursor.getString(qualityIndex) ?: "" else "",
            streamType = if (typeIndex >= 0) cursor.getString(typeIndex) ?: "" else "",
            title = if (titleIndex >= 0) cursor.getString(titleIndex) else null,
            headers = if (headersIndex >= 0) parseHeaders(cursor.getString(headersIndex)) else emptyMap(),
            subtitles = if (subtitlesIndex >= 0) parseSubtitles(cursor.getString(subtitlesIndex)) else emptyList(),
          )
          streams.add(stream)
        }
      }

      if (streams.isEmpty()) {
        Result.Failure(PluginError.NoStreamsAvailable)
      } else {
        Result.Success(StreamResponse(streams))
      }
    } catch (e: Exception) {
      Result.Failure(PluginError.CommunicationError(e.message ?: "Unknown error"))
    }
  }

  private fun buildStreamUri(authority: String, request: StreamRequest): Uri {
    val builder = Uri.Builder()
      .scheme("content")
      .authority(authority)
      .appendPath(PluginContract.PATH_STREAM)

    when (request) {
      is StreamRequest.Movie -> {
        builder
          .appendPath(PluginContract.PATH_MOVIE)
          .appendQueryParameter(PluginContract.Stream.PARAM_TMDB_ID, request.tmdbId.toString())
          .appendQueryParameter(PluginContract.Stream.PARAM_TITLE, request.title)
          .appendQueryParameter(PluginContract.Stream.PARAM_YEAR, request.year.toString())
        request.imdbId?.let {
          builder.appendQueryParameter(PluginContract.Stream.PARAM_IMDB_ID, it)
        }
      }
      is StreamRequest.Show -> {
        builder
          .appendPath(PluginContract.PATH_SHOW)
          .appendQueryParameter(PluginContract.Stream.PARAM_TMDB_ID, request.tmdbId.toString())
          .appendQueryParameter(PluginContract.Stream.PARAM_TITLE, request.title)
          .appendQueryParameter(PluginContract.Stream.PARAM_YEAR, request.year.toString())
          .appendQueryParameter(PluginContract.Stream.PARAM_SEASON, request.season.toString())
          .appendQueryParameter(PluginContract.Stream.PARAM_EPISODE, request.episode.toString())
        request.imdbId?.let {
          builder.appendQueryParameter(PluginContract.Stream.PARAM_IMDB_ID, it)
        }
      }
    }

    return builder.build()
  }

  private fun isValidStreamUrl(url: String): Boolean {
    return url.startsWith("http://") || url.startsWith("https://")
  }

  private fun parseHeaders(headersJson: String?): Map<String, String> {
    if (headersJson.isNullOrEmpty()) return emptyMap()
    return try {
      val json = JSONObject(headersJson)
      val result = mutableMapOf<String, String>()
      json.keys().forEach { key ->
        val value = json.optString(key)
        if (isSafeHeader(key)) {
          result[key] = value
        }
      }
      result
    } catch (e: Exception) {
      emptyMap()
    }
  }

  private fun isSafeHeader(headerName: String): Boolean {
    val dangerousHeaders = setOf(
      "host",
      "connection",
      "content-length",
      "transfer-encoding",
      "upgrade",
      "proxy-connection",
      "proxy-authenticate",
      "proxy-authorization",
    )
    return headerName.lowercase() !in dangerousHeaders
  }

  private fun parseSubtitles(subtitlesJson: String?): List<PluginSubtitle> {
    if (subtitlesJson.isNullOrEmpty()) return emptyList()
    return try {
      val jsonArray = JSONArray(subtitlesJson)
      (0 until jsonArray.length()).mapNotNull { i ->
        val obj = jsonArray.optJSONObject(i)
        val url = obj?.optString("url") ?: return@mapNotNull null
        if (!isValidStreamUrl(url)) return@mapNotNull null
        PluginSubtitle(
          url = url,
          language = obj.optString("language", "unknown"),
          label = obj.optString("label"),
        )
      }
    } catch (e: Exception) {
      emptyList()
    }
  }

  override suspend fun getPluginAuthState(pluginId: String): Result<PluginAuthState, PluginError> =
    withContext(Dispatchers.IO) {
      val plugin = _plugins.value.find { it.pluginId == pluginId }
        ?: return@withContext Result.Failure(PluginError.PluginNotFound)

      if (!plugin.requiresAuth) {
        return@withContext Result.Success(PluginAuthState.NotRequired)
      }

      try {
        val uri = Uri.parse("content://${plugin.authority}")
        val result = contentResolver.call(uri, PluginContract.Auth.METHOD_GET_AUTH_STATE, null, null)

        if (result == null) {
          return@withContext Result.Success(PluginAuthState.NotAuthenticated)
        }

        val isAuthenticated = result.getBoolean(PluginContract.Auth.KEY_IS_AUTHENTICATED, false)
        if (isAuthenticated) {
          Result.Success(PluginAuthState.Authenticated)
        } else {
          Result.Success(PluginAuthState.NotAuthenticated)
        }
      } catch (e: Exception) {
        Result.Failure(PluginError.CommunicationError(e.message ?: "Unknown error"))
      }
    }

  override suspend fun logoutPlugin(pluginId: String): Result<Boolean, PluginError> =
    withContext(Dispatchers.IO) {
      val plugin = _plugins.value.find { it.pluginId == pluginId }
        ?: return@withContext Result.Failure(PluginError.PluginNotFound)

      try {
        val uri = Uri.parse("content://${plugin.authority}")
        val result = contentResolver.call(uri, PluginContract.Auth.METHOD_LOGOUT, null, null)

        val success = result?.getBoolean(PluginContract.Auth.KEY_LOGOUT_SUCCESS, false) ?: false
        Result.Success(success)
      } catch (e: Exception) {
        Result.Failure(PluginError.CommunicationError(e.message ?: "Unknown error"))
      }
    }
}
