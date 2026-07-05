import com.android.build.api.variant.BuildConfigField

plugins {
  id("io.filmtime.gradle.android.data")
  id("kotlinx-serialization")
}

android {
  namespace = "io.filmtime.data.network"

  buildFeatures {
    buildConfig = true
  }
}

androidComponents {
  onVariants { variant ->
    variant.buildConfigFields?.put(
      "TMDB_API_KEY",
      BuildConfigField("String", "\"${project.properties["FILM_TIME_TMDB_API_KEY"]}\"", null),
    )
    variant.buildConfigFields?.put(
      "TRAKT_CLIENT_ID",
      BuildConfigField("String", "\"${project.properties["FILM_TIME_TRAKT_CLIENT_ID"]}\"", null),
    )
    variant.buildConfigFields?.put(
      "TRAKT_CLIENT_SECRET",
      BuildConfigField("String", "\"${project.properties["FILM_TIME_TRAKT_CLIENT_SECRET"]}\"", null),
    )
  }
}

dependencies {
  implementation(libs.retrofit)
  implementation(libs.retrofit.kotlinx.serialization)
  implementation(libs.kotlinx.serialization.json)
}
