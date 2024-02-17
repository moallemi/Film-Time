package io.filmtime.data.model

data class VideoThumbnail(
  val ids: VideoId,
  val title: String,
  val posterUrl: String,
  val year: Int,
  val type: VideoType,
) {
  val isMovie: Boolean
    get() = type == VideoType.Movie

  val isShow: Boolean
    get() = type == VideoType.Show
}

data class VideoId(
  val traktId: Int?,
  val tmdbId: Int?,
)

enum class VideoType {
  Movie,
  Show,
}

enum class VideoListType {
  Trending,
  Popular,
  TopRated,
  OnTheAir,
  AiringToday,
}
