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
}
