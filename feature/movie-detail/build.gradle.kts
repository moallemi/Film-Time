plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.movie.detail"
}

dependencies {
  implementation(project(":core:browser"))
  implementation(project(":core:plugin-api"))
  implementation(project(":data:model"))

  implementation(project(":domain:tmdb-movies"))
  implementation(project(":domain:bookmarks"))
  implementation(project(":domain:trakt:trakt"))
  implementation(project(":domain:plugin"))

  implementation(project(":feature:trakt-buttons"))
  implementation(project(":feature:credits"))
  implementation(project(":feature:similar"))
  implementation(project(":feature:plugin-manager"))

  testImplementation(project(":domain:testing"))
  testImplementation(libs.coroutines.test)
  testImplementation(libs.turbine)
}
