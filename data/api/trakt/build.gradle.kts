plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.api.trakt"
}

dependencies {
  implementation(project(":data:network"))
  implementation(project(":data:storage:trakt"))
}
