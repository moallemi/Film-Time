plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.player"
}

dependencies {
  implementation(libs.androidx.media3.exoplayer)
  implementation(libs.androidx.media3.hls)
  implementation(libs.androidx.media3.ui)
}
