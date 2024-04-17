plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.storage.trakt"
}

dependencies {
  implementation(libs.datastore)
  implementation(libs.core.ktx)
}
