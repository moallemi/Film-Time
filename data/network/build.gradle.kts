plugins {
  id("io.filmtime.gradle.android.data")
  id("kotlinx-serialization")
}

android {
  namespace = "io.filmtime.data.network"

  defaultConfig {
    buildConfigField("String", "TMDB_API_KEY", "\"${project.properties["FILM_TIME_TMDB_API_KEY"]}\"")
    buildConfigField("String", "TRAKT_CLIENT_ID", "\"${project.properties["FILM_TIME_TRAKT_CLIENT_ID"]}\"")
    buildConfigField("String", "TRAKT_CLIENT_SECRET", "\"${project.properties["FILM_TIME_TRAKT_CLIENT_SECRET"]}\"")
  }

  buildFeatures {
    buildConfig = true
  }
}

dependencies {
  implementation(libs.retrofit)
  implementation(libs.retrofit.kotlinx.serialization)
  implementation(libs.kotlinx.serialization.json)
}
