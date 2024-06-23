package io.filmtime.data.model

data class VideoDetail(
  val ids: VideoId,
  val title: String,
  val posterUrl: String,
  val coverUrl: String,
  val year: Int,
  val genres: List<String>,
  val originalLanguage: String?,
  val spokenLanguages: List<String>,
  val description: String,
  val isWatched: Boolean? = null,
  val runtime: String?,
  val releaseDate: String,
  val tagline: String?,
  val budget: Long?,
  val homePage: String?,
  val status: String?,
  val voteAverage: Float = 0.0F,
  val voteColor: Long = 0,
) {
  companion object
}

val VideoDetail.Companion.PreviewMovie: VideoDetail
  get() = VideoDetail(
    ids = VideoId(1, 1),
    title = "Furiosa: A Mad Max Saga",
    posterUrl = "",
    coverUrl = "",
    year = 2024,
    genres = listOf("Action", "Adventure"),
    originalLanguage = "en",
    spokenLanguages = listOf("en"),
    description = "Snatched from the Green Place of Many Mothers, young Furiosa falls into the hands of a great biker" +
      " horde led by the warlord Dementus. Sweeping through the Wasteland, they come across the Citadel, presided " +
      "over by the Immortan Joe. As the two tyrants fight for dominance, Furiosa soon finds herself in a nonstop" +
      " battle to make her way home.",
    runtime = "120 min",
    releaseDate = "2021-01-01",
    voteAverage = 7.5F,
    voteColor = 0xFF00FF00,
    budget = 100_000_000,
    homePage = "https://www.furiosa.com",
    status = "Released",
    tagline = "The Future Belongs to the Mad",
  )

val VideoDetail.Companion.PreviewShow: VideoDetail
  get() = VideoDetail(
    ids = VideoId(1, 1),
    title = "The Witcher",
    posterUrl = "",
    coverUrl = "",
    year = 2019,
    genres = listOf("Action", "Adventure"),
    originalLanguage = "en",
    spokenLanguages = listOf("en"),
    description = "Geralt of Rivia, a mutated monster-hunter for hire, journeys toward his destiny in a turbulent" +
      " world where people often prove more wicked than beasts.",
    runtime = "60 min",
    releaseDate = "2019-12-20",
    voteAverage = 8.5F,
    voteColor = 0xFF00FF00,
    status = "Released",
    budget = null,
    homePage = "https://www.witcher.com",
    tagline = null,
  )
