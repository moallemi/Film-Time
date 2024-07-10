plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.tmdb.person"
}

dependencies {
  implementation(project(":data:tmdb-person"))
}
