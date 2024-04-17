plugins {
  id("io.filmtime.gradle.android.domain")
}

android {
  namespace = "io.filmtime.domain.trakt.auth"
}

dependencies {
  implementation(project(":data:trakt-auth"))
  implementation(project(":data:storage:trakt"))
}
