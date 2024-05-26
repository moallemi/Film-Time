package io.filmtime.gradle.plugins

import io.filmtime.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class HiltPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.google.devtools.ksp")
        apply("dagger.hilt.android.plugin")
      }

      dependencies {
        "implementation"(libs.findLibrary("hilt.android").get())
        "ksp"(libs.findLibrary("dagger.hilt.android.compiler").get())
      }
    }
  }
}
