plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.search"
}

dependencies {
  implementation(project(":data:model"))
  implementation(project(":domain:tmdb-search"))

  api(libs.paging.compose)
}
