package io.filmtime.data.model

data class EpisodeThumbnail(
  val ids: VideoId,
  val episodeNumber: Int,
  val title: String,
  val description: String,
  val posterUrl: String,
  val airDate: String,
  val isWatched: Boolean = false,
) {
  companion object
}

val EpisodeThumbnail.Companion.Preview: EpisodeThumbnail
  get() = EpisodeThumbnail(
    ids = VideoId(1, 1),
    episodeNumber = 6,
    title = "The Witcher",
    description = "Geralt of Rivia, a solitary monster hunter, struggles to find his place in a world where people" +
      " often prove more wicked than beasts.",
    posterUrl = "",
    airDate = "2021-01-01",
    isWatched = true,
  )
