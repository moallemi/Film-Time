plugins {
  id("io.filmtime.gradle.android.data")
  alias(libs.plugins.ksp)
}

android {
  namespace = "io.filmtime.data.database"
}

dependencies {
  implementation(libs.room.runtime)
  implementation(libs.room.ktx)
  ksp(libs.room.compiler)
}
