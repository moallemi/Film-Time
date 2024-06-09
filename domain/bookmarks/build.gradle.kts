plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.bookmarks"
}

dependencies {
  implementation(project(":data:bookmarks"))
}
