plugins {
  id("io.filmtime.gradle.android.library")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.core.designsystem"
}

dependencies {

  implementation(project(":data:model"))

  implementation(libs.core.ktx)
  implementation(libs.appcompat)
  implementation(libs.material)

  api(platform(libs.compose.bom))
  api(libs.ui)
  api(libs.material3)
  api(libs.icons.extended)

  api(libs.coil.compose)

  testImplementation(libs.junit)
  androidTestImplementation(libs.androidx.test.ext.junit)
  androidTestImplementation(libs.espresso.core)
}
