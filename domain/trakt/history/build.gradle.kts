plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.trakt.history"
}

dependencies {
  implementation(project(":data:trakt"))
}
