package io.filmtime.data.network

import io.filmtime.data.network.MediaType.Movie
import io.filmtime.data.network.MediaType.Person
import io.filmtime.data.network.MediaType.TV
import kotlinx.serialization.KSerializer
import kotlinx.serialization.PolymorphicSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable
data class TmdbSearchListResponse(

  val page: Long? = null,
  val results: List<NetworkSearchResult>? = null,

  @SerialName("total_pages")
  val totalPages: Long? = null,

  @SerialName("total_results")
  val totalResults: Long? = null,
)

@Serializable(with = SearchResultSerializer::class)
sealed interface NetworkSearchResult {

  @SerialName("media_type")
  val mediaType: MediaType?
}

@Serializable
data class TvShowSearchResult(
  val adult: Boolean? = null,

  @SerialName("backdrop_path")
  val backdropPath: String? = null,

  val id: Int? = null,
  val title: String? = null,

  @SerialName("original_language")
  val originalLanguage: String? = null,

  @SerialName("original_title")
  val originalTitle: String? = null,

  val overview: String? = null,

  @SerialName("poster_path")
  val posterPath: String? = null,

  @SerialName("genre_ids")
  val genreIDS: List<Long>? = null,

  val popularity: Double? = null,

  @SerialName("release_date")
  val releaseDate: String? = null,

  val video: Boolean? = null,

  @SerialName("vote_average")
  val voteAverage: Double? = null,

  @SerialName("vote_count")
  val voteCount: Long? = null,
) : NetworkSearchResult {
  override val mediaType: MediaType
    get() = TV
}

@Serializable
data class MovieSearchResult(
  val adult: Boolean? = null,

  @SerialName("backdrop_path")
  val backdropPath: String? = null,

  val id: Int? = null,
  val title: String? = null,

  @SerialName("original_language")
  val originalLanguage: String? = null,

  @SerialName("original_title")
  val originalTitle: String? = null,

  val overview: String? = null,

  @SerialName("poster_path")
  val posterPath: String? = null,

  @SerialName("genre_ids")
  val genreIDS: List<Long>? = null,

  val popularity: Double? = null,

  @SerialName("release_date")
  val releaseDate: String? = null,

  val video: Boolean? = null,

  @SerialName("vote_average")
  val voteAverage: Double? = null,

  @SerialName("vote_count")
  val voteCount: Long? = null,
) : NetworkSearchResult {
  override val mediaType: MediaType
    get() = Movie
}

@Serializable
data class PersonSearchResult(
  val name: String,
) : NetworkSearchResult {
  override val mediaType: MediaType
    get() = Person
}

internal class SearchResultSerializer : KSerializer<NetworkSearchResult> {

  private val json = Json {
    ignoreUnknownKeys = true
    isLenient = true
    encodeDefaults = true
    prettyPrint = true
    coerceInputValues = true
  }
  override val descriptor: SerialDescriptor
    get() = PolymorphicSerializer(NetworkSearchResult::class).descriptor

  override fun deserialize(decoder: Decoder): NetworkSearchResult {
    val jsonElement = (decoder as JsonDecoder).decodeJsonElement()
    return when (val itemType = jsonElement.jsonObject["media_type"]?.jsonPrimitive?.content) {
      "movie" -> json.decodeFromJsonElement(MovieSearchResult.serializer(), jsonElement)
      "tv" -> json.decodeFromJsonElement(TvShowSearchResult.serializer(), jsonElement)
      "person" -> json.decodeFromJsonElement(PersonSearchResult.serializer(), jsonElement)
      else -> throw SerializationException("Unknown itemType: $itemType")
    }
  }

  override fun serialize(encoder: Encoder, value: NetworkSearchResult) {
    when (value) {
      is TvShowSearchResult -> encoder.encodeSerializableValue(TvShowSearchResult.serializer(), value)
      is PersonSearchResult -> encoder.encodeSerializableValue(PersonSearchResult.serializer(), value)
      is MovieSearchResult -> encoder.encodeSerializableValue(MovieSearchResult.serializer(), value)
    }
  }
}
