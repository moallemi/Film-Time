@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.com.android.library)
  alias(libs.plugins.org.jetbrains.kotlin.android)

  kotlin("kapt")
  alias(libs.plugins.hilt.android)
}

android {
  namespace = "io.fimltime.data.tmdb.movies"
  compileSdk = 33

  defaultConfig {
    minSdk = 27

    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
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
  implementation(project(":data:api:tmdb"))
  implementation(project(":data:model"))

  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.material)

  implementation(libs.hilt.android)

  kapt(libs.dagger.hilt.android.compiler)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)

  implementation(libs.paging)

  implementation(libs.square.okhttp)
  implementation(libs.square.okhttp.logging)
  implementation(libs.retrofit)
}
