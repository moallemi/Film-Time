plugins {
  id("io.filmtime.gradle.android.data")
}

android {
  namespace = "io.filmtime.data.bookmarks"
}

dependencies {
  implementation(project(":data:database"))
}
