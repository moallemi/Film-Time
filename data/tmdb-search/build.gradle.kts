plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.tmdb.search"
}

dependencies {

  implementation(project(":data:api:tmdb"))

  api(libs.paging.runtime)
}
