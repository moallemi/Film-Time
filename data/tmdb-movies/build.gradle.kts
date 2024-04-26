plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.tmdb.movies"
}

dependencies {
  implementation(project(":data:api:tmdb"))
  implementation(project(":data:api:trakt"))

  api(libs.paging.runtime)
  implementation(project(":data:database"))
}
