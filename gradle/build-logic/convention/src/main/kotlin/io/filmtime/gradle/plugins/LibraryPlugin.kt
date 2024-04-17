package io.filmtime.gradle.plugins

import com.android.build.gradle.LibraryExtension
import io.filmtime.gradle.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class LibraryPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
      }

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid()
        defaultConfig.targetSdk = 34
      }

      dependencies {
        add("testImplementation", kotlin("test"))
      }
    }
  }
}
