package io.filmtime.gradle.plugins

import io.filmtime.gradle.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.project

class FeaturePlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply {
        apply("io.filmtime.gradle.android.library")
        apply("io.filmtime.gradle.android.hilt")
      }

      dependencies {
        add("implementation", project(":data:model"))
        add("implementation", project(":core:ui:common"))
        add("implementation", project(":core:resources"))
        add("implementation", project(":core:ui:navigation"))
        add("implementation", project(":core:design-system"))

        add("implementation", libs.findLibrary("androidx.hilt.navigation.compose").get())
        add("implementation", libs.findLibrary("lifecycle.viewmodel.compose.runtime").get())
        add("implementation", libs.findLibrary("lifecycle.viewmodel.compose").get())
      }
    }
  }
}
