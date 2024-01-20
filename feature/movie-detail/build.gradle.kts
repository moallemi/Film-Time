@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.com.android.library)
  alias(libs.plugins.org.jetbrains.kotlin.android)

  kotlin("kapt")
  alias(libs.plugins.hilt.android)
}

android {
  namespace = "io.filmtime.feature.movie.detail"
  compileSdk = 34

  defaultConfig {
    minSdk = 27

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildFeatures {
    compose = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.8"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }
}

dependencies {

  implementation(project(":data:model"))
  implementation(project(":domain:tmdb-movies"))
  implementation(project(":domain:stream"))
  implementation(project(":data:trakt"))

  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.material)

  implementation(platform(libs.compose.bom))
  implementation(libs.ui)
  implementation(libs.ui.graphics)
  implementation(libs.ui.tooling.preview)
  implementation(libs.material3)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.hilt.navigation.compose)

  implementation(libs.coil.compose)

  implementation(libs.hilt.android)
  kapt(libs.dagger.hilt.android.compiler)

  implementation(libs.lifecycle.viewmodel.compose)
  implementation(libs.lifecycle.viewmodel.compose.runtime)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
  implementation(libs.lottie)
}
