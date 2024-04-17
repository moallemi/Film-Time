package io.filmtime.gradle.plugins

import com.android.build.gradle.LibraryExtension
import io.filmtime.gradle.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class LibraryComposePlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      pluginManager.apply("com.android.library")

      val extension = extensions.getByType<LibraryExtension>()
      configureAndroidCompose(extension)
    }
  }
}
