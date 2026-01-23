plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.plugin"
}

dependencies {
  implementation(project(":core:plugin-api"))
  implementation(project(":data:plugin-discovery"))
}
