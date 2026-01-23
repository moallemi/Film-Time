plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.plugin.manager"
}

dependencies {
  implementation(project(":core:plugin-api"))
  implementation(project(":domain:plugin"))
}
