plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.trakt.login"

  defaultConfig {
    buildConfigField("String", "TRAKT_CLIENT_ID", "\"${project.properties["FILM_TIME_TRAKT_CLIENT_ID"]}\"")

    buildFeatures {
      buildConfig = true
    }
  }
}

dependencies {
  implementation(project(":core:browser"))

  implementation(project(":domain:trakt:auth"))
}
