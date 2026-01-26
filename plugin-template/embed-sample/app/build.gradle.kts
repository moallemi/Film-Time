import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  id("com.android.application")
  id("org.jetbrains.kotlin.android")
}

android {
  namespace = "io.filmtime.plugin.embed"
  compileSdk = 36

  defaultConfig {
    applicationId = "io.filmtime.plugin.embed"
    minSdk = 24
    targetSdk = 36
    versionCode = 1
    versionName = "1.0.0"
  }

  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.fromTarget("17")
  }
}

dependencies {
  implementation("androidx.core:core-ktx:1.17.0")
}
