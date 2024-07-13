import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
  `kotlin-dsl`
}

group = "io.filmtime.build"

java {
  sourceCompatibility = JavaVersion.VERSION_17
  targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
  compilerOptions {
    jvmTarget = JvmTarget.JVM_17
  }
}

dependencies {
  compileOnly(libs.android.gradlePlugin)
  compileOnly(libs.kotlin.gradlePlugin)
  compileOnly(libs.ksp.gradlePlugin)
  compileOnly(libs.room.gradlePlugin)
  compileOnly(libs.compose.gradlePlugin)
  compileOnly(libs.google.play.publish.gradlePlugin)
  compileOnly(libs.advanced.gradle.build.version.gradlePlugin)
}

tasks {
  validatePlugins {
    enableStricterValidation = true
    failOnWarning = true
  }
}

gradlePlugin {
  plugins {
    register("androidApplicationCompose") {
      id = "io.filmtime.gradle.android.application.compose"
      implementationClass = "io.filmtime.gradle.plugins.ApplicationComposePlugin"
    }
    register("androidApplication") {
      id = "io.filmtime.gradle.android.application"
      implementationClass = "io.filmtime.gradle.plugins.ApplicationPlugin"
    }
    register("androidLibraryCompose") {
      id = "io.filmtime.gradle.android.library.compose"
      implementationClass = "io.filmtime.gradle.plugins.LibraryComposePlugin"
    }
    register("androidLibrary") {
      id = "io.filmtime.gradle.android.library"
      implementationClass = "io.filmtime.gradle.plugins.LibraryPlugin"
    }
    register("androidFeature") {
      id = "io.filmtime.gradle.android.feature"
      implementationClass = "io.filmtime.gradle.plugins.FeaturePlugin"
    }
    register("androidData") {
      id = "io.filmtime.gradle.android.data"
      implementationClass = "io.filmtime.gradle.plugins.DataPlugin"
    }
    register("androidDomain") {
      id = "io.filmtime.gradle.android.domain"
      implementationClass = "io.filmtime.gradle.plugins.DomainPlugin"
    }
    register("androidHilt") {
      id = "io.filmtime.gradle.android.hilt"
      implementationClass = "io.filmtime.gradle.plugins.HiltPlugin"
    }
    register("jvmLibrary") {
      id = "io.filmtime.gradle.jvm.library"
      implementationClass = "io.filmtime.gradle.plugins.LibraryJvmPlugin"
    }
    register("roomLibrary") {
      id = "io.filmtime.gradle.android.room"
      implementationClass = "io.filmtime.gradle.plugins.RoomPlugin"
    }
  }
}
