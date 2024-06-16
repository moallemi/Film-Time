plugins {
  id("io.filmtime.gradle.android.feature")
  id("io.filmtime.gradle.android.library.compose")
}

android {
  namespace = "io.filmtime.feature.movie.detail"
}

dependencies {

  implementation(project(":data:model"))
  implementation(project(":core:ui:common"))

  implementation(project(":domain:tmdb-movies"))
  implementation(project(":domain:stream"))
  implementation(project(":domain:bookmarks"))

  implementation(project(":feature:trakt-buttons"))
  implementation(project(":feature:credits"))
}
