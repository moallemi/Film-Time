package io.filmtime.gradle.plugins

import com.android.build.api.dsl.ApplicationExtension
import io.filmtime.gradle.configureFlavors
import io.filmtime.gradle.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class ApplicationPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")

        apply("io.filmtime.gradle.android.application.compose")
        apply("io.filmtime.gradle.android.hilt")
      }

      extensions.configure<ApplicationExtension> {
        configureKotlinAndroid()

        defaultConfig {
          targetSdk = 34
        }

        configureFlavors(this)

        signingConfigs {
          getByName("debug") {
            storeFile = rootProject.file("release/filmtime-debug.jks")
            storePassword = "keystorepassword"
            keyAlias = "filmtime"
            keyPassword = "aliaspassword"
          }

          create("release") {
            if (rootProject.file("release/filmtime-release.jks").exists()) {
              storeFile = rootProject.file("release/filmtime-release.jks")
              storePassword = properties["FILM_TIME_RELEASE_KEYSTORE_PASSWORD"]?.toString() ?: ""
              keyAlias = "key0"
              keyPassword = properties["FILM_TIME_RELEASE_KEY_PASSWORD"]?.toString() ?: ""
            }
          }
        }

        buildTypes {
          debug {
            signingConfig = signingConfigs.findByName("debug")
          }

          release {
            signingConfig = signingConfigs.findByName("release")
            isShrinkResources = false
            isMinifyEnabled = false
            proguardFiles("proguard-rules.pro")
          }
        }
      }

      dependencies {
        add("implementation", project(":core:design-system"))
        add("implementation", project(":core:resources"))
      }
    }
  }
}
