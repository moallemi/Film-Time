plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.tmdb.movies"
}

dependencies {
  implementation(project(":data:tmdb-movies"))
}
