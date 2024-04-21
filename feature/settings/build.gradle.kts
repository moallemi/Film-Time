plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.settings"
}

dependencies {
  implementation(project(":domain:trakt:auth"))
}
