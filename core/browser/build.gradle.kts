plugins {
  id("io.filmtime.gradle.android.library")
}

android {
  namespace = "io.filmtime.core.browser"
}

dependencies {
  implementation(project(":core:ui:common"))

  implementation(libs.androidx.browser)
}
