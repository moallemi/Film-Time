plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.trakt"
}

dependencies {
  implementation(project(":data:api:trakt"))
}
