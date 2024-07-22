plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.video.thumbnail.grid.genre"
}

dependencies {
  implementation(project(":domain:tmdb-genre"))
  api(libs.paging.compose)
}
