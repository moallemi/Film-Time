package io.filmtime.gradle.plugins

import com.android.build.api.dsl.ApplicationExtension
import io.filmtime.gradle.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

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
      }
    }
  }
}
