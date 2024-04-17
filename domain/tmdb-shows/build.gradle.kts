plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.tmdb.shows"
}

dependencies {
  implementation(project(":data:tmdb-shows"))
}
