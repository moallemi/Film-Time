plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.trakt.buttons"
}

dependencies {
  implementation(project(":domain:trakt:history"))
}
