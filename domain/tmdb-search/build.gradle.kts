plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.tmdb.search"
}

dependencies {

  implementation(project(":data:tmdb-search"))
}
