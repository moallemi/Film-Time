plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.plugin.discovery"
}

dependencies {
  implementation(project(":core:plugin-api"))
}
