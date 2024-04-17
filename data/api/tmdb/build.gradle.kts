plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.api.tmdb"
}

dependencies {
  implementation(project(":data:network"))
}
