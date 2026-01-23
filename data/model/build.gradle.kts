plugins {
  id("io.filmtime.gradle.jvm.library")
  alias(libs.plugins.kotlinx.serialization)
}

dependencies {
  implementation(libs.kotlinx.serialization.json)
}
