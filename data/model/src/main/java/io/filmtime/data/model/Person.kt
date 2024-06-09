package io.filmtime.data.model

data class Person(
  val id: Long?,
  val name: String,
  val imageUrl: String,
  val role: String,
  val type: PersonType,
) {
  companion object
}

enum class PersonType {
  Cast,
  Crew,
}

val Person.Companion.PreviewCast: Person
  get() = Person(
    id = 1,
    name = "John Doe",
    imageUrl = "",
    role = "Actor",
    type = PersonType.Cast,
  )

val Person.Companion.PreviewCrew: Person
  get() = Person(
    id = 2,
    name = "Amy Doe",
    imageUrl = "",
    role = "Director",
    type = PersonType.Crew,
  )
