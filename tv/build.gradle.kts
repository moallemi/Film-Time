plugins {
  id("io.filmtime.gradle.application.tv")
  alias(libs.plugins.kotlinx.serialization)
}

android {
  namespace = "io.filmtime.tv"

  defaultConfig {
    applicationId = "io.filmtime.tv"
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {
  implementation(project(":core:ui:navigation"))
  implementation(project(":data:model"))

  implementation(project(":core:ui:common"))

  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.lifecycle.runtime.ktx)
  implementation(libs.activity.compose)
  implementation(libs.icons.extended)
  implementation(project(":domain:tmdb-movies"))
  implementation(project(":domain:tmdb-shows"))
  implementation(project(":domain:stream"))
  implementation(project(":domain:bookmarks"))
  implementation(project(":domain:trakt:trakt"))
  implementation(project(":domain:trakt:history"))
  implementation(libs.androidx.hilt.navigation.compose)
  implementation(libs.lifecycle.viewmodel.compose.runtime)
  implementation(libs.coil.compose)
  implementation(libs.kotlinx.serialization.json)
}
