package io.filmtime.core.plugin.api

object PluginContract {

  const val AUTHORITY_PREFIX = "io.filmtime.plugin"
  const val ACTION_STREAM_PROVIDER = "io.filmtime.plugin.STREAM_PROVIDER"

  const val PATH_METADATA = "metadata"
  const val PATH_STREAM = "stream"
  const val PATH_MOVIE = "movie"
  const val PATH_SHOW = "show"

  object Metadata {
    const val COLUMN_PLUGIN_ID = "plugin_id"
    const val COLUMN_NAME = "name"
    const val COLUMN_DESCRIPTION = "description"
    const val COLUMN_VERSION = "version"
    const val COLUMN_ICON_URL = "icon_url"
    const val COLUMN_REQUIRES_AUTH = "requires_auth"
    const val COLUMN_LOGIN_ACTIVITY = "login_activity"
  }

  object Stream {
    const val PARAM_TMDB_ID = "tmdb_id"
    const val PARAM_TITLE = "title"
    const val PARAM_YEAR = "year"
    const val PARAM_SEASON = "season"
    const val PARAM_EPISODE = "episode"
    const val PARAM_IMDB_ID = "imdb_id"

    const val COLUMN_STREAM_URL = "stream_url"
    const val COLUMN_QUALITY = "quality"
    const val COLUMN_STREAM_TYPE = "stream_type"
    const val COLUMN_TITLE = "title"
    const val COLUMN_HEADERS = "headers"
    const val COLUMN_SUBTITLES = "subtitles"

    const val EXTRA_AUTH_REQUIRED = "auth_required"
  }

  object StreamType {
    const val HLS = "hls"
    const val DASH = "dash"
    const val MP4 = "mp4"
    const val UNKNOWN = "unknown"
  }

  object Quality {
    const val AUTO = "auto"
    const val SD = "sd"
    const val HD = "hd"
    const val FHD = "fhd"
    const val UHD = "uhd"
  }

  object Auth {
    const val METHOD_GET_AUTH_STATE = "get_auth_state"
    const val METHOD_LOGOUT = "logout"
    const val KEY_REQUIRES_AUTH = "requires_auth"
    const val KEY_IS_AUTHENTICATED = "is_authenticated"
    const val KEY_AUTH_DISPLAY_NAME = "auth_display_name"
    const val KEY_LOGOUT_SUCCESS = "logout_success"
    const val ACTION_LOGIN = "io.filmtime.plugin.ACTION_LOGIN"
    const val EXTRA_LOGIN_RESULT = "login_result"
    const val LOGIN_RESULT_SUCCESS = 1
    const val LOGIN_RESULT_CANCELLED = 2
    const val LOGIN_RESULT_ERROR = 3
    const val EXTRA_ERROR_MESSAGE = "error_message"
  }
}
