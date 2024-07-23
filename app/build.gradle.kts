plugins {
  id("io.filmtime.gradle.android.application")
}

android {
  namespace = "io.filmtime"

  defaultConfig {
    applicationId = "io.filmtime"
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation(project(":data:model"))
  implementation(project(":core:ui:navigation"))

  implementation(project(":feature:movie-detail"))
  implementation(project(":feature:show-detail"))
  implementation(project(":feature:home"))
  implementation(project(":feature:movies"))
  implementation(project(":feature:shows"))
  implementation(project(":feature:settings"))
  implementation(project(":feature:player"))
  implementation(project(":feature:trakt-login"))
  implementation(project(":feature:search"))
  implementation(project(":feature:video-thumbnail-grid"))
  implementation(project(":feature:video-thumbnail-grid-genre"))

  implementation(project(":core:libs:logger"))
  implementation(project(":core:libs:tracker"))

  implementation(libs.activity.compose)
  implementation(libs.material3)
  implementation(libs.lifecycle.viewmodel.compose.runtime)
  implementation(libs.androidx.hilt.navigation.compose)
}

if (file("google-services.json").exists()) {
  apply(plugin = libs.plugins.google.services.get().pluginId)
  apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
}
