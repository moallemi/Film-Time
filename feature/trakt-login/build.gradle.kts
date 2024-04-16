@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.com.android.library)
  alias(libs.plugins.org.jetbrains.kotlin.android)

  kotlin("kapt")
  alias(libs.plugins.hilt.android)
}

android {
  namespace = "io.filmtime.feature.trakt.login"
  compileSdk = 34

  defaultConfig {
    minSdk = 27

    buildConfigField("String", "TRAKT_CLIENT_ID", "\"${System.getenv("FILM_TIME_TRAKT_CLIENT_ID")}\"")

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildFeatures {
    compose = true
    buildConfig = true
  }

  composeOptions {
    kotlinCompilerExtensionVersion = "1.5.10"
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro",
      )
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
  implementation(project(":domain:trakt:auth"))

  implementation(platform(libs.compose.bom))
  implementation(libs.ui)
  implementation(libs.ui.graphics)
  implementation(libs.material3)

  implementation(libs.hilt.android)
  kapt(libs.dagger.hilt.android.compiler)

  implementation(libs.lifecycle.viewmodel.compose)
  implementation(libs.lifecycle.viewmodel.compose.runtime)

  implementation(libs.androidx.navigation.compose)
  implementation(libs.androidx.hilt.navigation.compose)

  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.material)
  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
}
