plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.credits"
}

dependencies {
  implementation(project(":data:model"))
  implementation(project(":domain:tmdb-movies"))
  implementation(project(":domain:tmdb-shows"))
}
