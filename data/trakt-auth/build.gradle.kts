plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.trakt.auth"
}

dependencies {
  implementation(project(":data:storage:trakt"))
  implementation(project(":data:api:trakt"))
}
