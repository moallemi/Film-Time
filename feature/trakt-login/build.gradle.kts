import com.android.build.api.variant.BuildConfigField

plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.trakt.login"

  buildFeatures {
    buildConfig = true
  }
}

androidComponents {
  onVariants { variant ->
    variant.buildConfigFields?.put(
      "TRAKT_CLIENT_ID",
      BuildConfigField("String", "\"${project.properties["FILM_TIME_TRAKT_CLIENT_ID"]}\"", null),
    )
  }
}

dependencies {
  implementation(project(":core:browser"))

  implementation(project(":domain:trakt:auth"))
}
