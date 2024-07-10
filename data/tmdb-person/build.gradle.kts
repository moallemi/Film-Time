plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.tmdb.person"
}

dependencies {
  implementation(project(":data:api:tmdb"))
}
