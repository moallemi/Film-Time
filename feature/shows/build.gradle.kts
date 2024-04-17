plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.shows"
}

dependencies {
  implementation(project(":domain:tmdb-shows"))
}
