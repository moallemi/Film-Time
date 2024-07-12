plugins {
  id("io.filmtime.gradle.android.library")
  id("io.filmtime.gradle.android.hilt")
}

android {
  namespace = "io.filmtime.core.libs.tracker"
}

dependencies {
  implementation(platform(libs.firebase.bom))
  implementation(libs.firebase.analytics)
  implementation(libs.firebase.crashlytics)
}
