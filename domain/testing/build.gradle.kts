plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.testing"
}

dependencies {
  implementation(project(":domain:tmdb-movies"))
  implementation(project(":domain:tmdb-shows"))
  implementation(project(":domain:plugin"))
  implementation(project(":core:plugin-api"))

  implementation(libs.junit)
  implementation(libs.coroutines.test)
}
