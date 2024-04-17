plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.video.thumbnail.grid"
}

dependencies {
  implementation(project(":domain:tmdb-movies"))
  implementation(project(":domain:tmdb-shows"))

  api(libs.paging.compose)
}
