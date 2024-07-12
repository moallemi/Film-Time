plugins {
  id("io.filmtime.gradle.android.library")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.core.ui.navigation"
}

dependencies {

  implementation(libs.androidx.navigation.compose)
}
