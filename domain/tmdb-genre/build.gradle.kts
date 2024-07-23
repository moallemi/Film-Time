plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.tmdb.genre"
}

dependencies {
  implementation(project(":data:tmdb-movies"))
  implementation(project(":data:tmdb-shows"))
}
