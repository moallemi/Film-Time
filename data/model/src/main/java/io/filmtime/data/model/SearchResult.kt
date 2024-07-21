package io.filmtime.data.model

sealed class SearchResult {
  data class Video(val item: VideoThumbnail) : SearchResult()

  data class TvShow(val item: VideoThumbnail) : SearchResult()

  data class Person(val name: String, val imageUrl: String) : SearchResult()
}
