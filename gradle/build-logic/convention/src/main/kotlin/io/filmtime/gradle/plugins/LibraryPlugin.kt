package io.filmtime.gradle.plugins

import com.android.build.api.dsl.LibraryExtension
import io.filmtime.gradle.Versions
import io.filmtime.gradle.configureFlavors
import io.filmtime.gradle.configureKotlinAndroid
import io.filmtime.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class LibraryPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
      }

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        testOptions.targetSdk = Versions.TARGET_SDK
        lint.targetSdk = Versions.TARGET_SDK
        configureFlavors(this)
      }

      dependencies {
        add("testImplementation", libs.findLibrary("kotlin-test").get())
        add("testImplementation", libs.findLibrary("junit").get())
      }
    }
  }
}
