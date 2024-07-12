import me.moallemi.gradle.advancedbuildversion.gradleextensions.VersionCodeType.GIT_COMMIT_COUNT

plugins {
  id("io.filmtime.gradle.android.application")
  alias(libs.plugins.advanced.gradle.build.version)
}

advancedVersioning {
  nameOptions {
    versionMajor(1)
    versionMinor(0)
    versionPatch(0)
  }

  codeOptions {
    versionCodeType(GIT_COMMIT_COUNT)
  }

  outputOptions {
    renameOutput(false)
    nameFormat("FilmTime-\${flavorName}-\${buildType}-\${versionName}-\${versionCode}")
  }
}

android {
  namespace = "io.filmtime"

  defaultConfig {
    applicationId = "io.filmtime"
    versionCode = advancedVersioning.versionCode
    versionName = advancedVersioning.versionName
  }

  packaging {
    resources {
      excludes += "/META-INF/{AL2.0,LGPL2.1}"
    }
  }
}

dependencies {

  implementation(project(":data:model"))
  implementation(project(":core:ui:common"))

  implementation(project(":feature:movie-detail"))
  implementation(project(":feature:show-detail"))
  implementation(project(":feature:home"))
  implementation(project(":feature:movies"))
  implementation(project(":feature:shows"))
  implementation(project(":feature:settings"))
  implementation(project(":feature:player"))
  implementation(project(":feature:trakt-login"))
  implementation(project(":feature:video-thumbnail-grid"))

  implementation(project(":core:libs:logger"))

  implementation(libs.activity.compose)
  implementation(libs.material3)
  implementation(libs.lifecycle.viewmodel.compose.runtime)
  implementation(libs.androidx.hilt.navigation.compose)
}

if (file("google-services.json").exists()) {
  apply(plugin = libs.plugins.google.services.get().pluginId)
  apply(plugin = libs.plugins.firebase.crashlytics.get().pluginId)
}
