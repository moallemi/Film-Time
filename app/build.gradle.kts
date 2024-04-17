plugins {
  id("io.filmtime.gradle.android.application")
}

android {
  namespace = "io.filmtime"

  defaultConfig {
    applicationId = "io.filmtime"
    versionCode = 1
    versionName = "1.0"
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation(project(":data:model"))
  implementation(project(":feature:movie-detail"))
  implementation(project(":feature:show-detail"))
  implementation(project(":feature:home"))
  implementation(project(":feature:movies"))
  implementation(project(":feature:shows"))
  implementation(project(":feature:settings"))
  implementation(project(":feature:player"))
  implementation(project(":feature:trakt-login"))
  implementation(project(":feature:video-thumbnail-grid"))

  implementation(libs.activity.compose)
  implementation(libs.material3)
  implementation(libs.lifecycle.viewmodel.compose.runtime)
  implementation(libs.androidx.hilt.navigation.compose)
}
