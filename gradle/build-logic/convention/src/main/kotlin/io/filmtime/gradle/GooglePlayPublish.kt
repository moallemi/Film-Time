package io.filmtime.gradle

import com.android.build.api.dsl.ApplicationExtension
import com.github.triplet.gradle.play.PlayPublisherExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.configure

internal fun Project.configureGooglePlayPublish() {
  if (!file("../release/google-play-publish.json").exists()) {
    return
  }

  with(pluginManager) {
    apply("com.github.triplet.play")
  }

  configure<PlayPublisherExtension> {
    enabled.set(false)
    defaultToAppBundles.set(true)
    serviceAccountCredentials.set(file("../release/google-play-publish.json"))
  }
}

internal fun Project.configureGooglePlayPublishFlavors(
  applicationExtension: ApplicationExtension,
) {
  if (!file("../release/google-play-publish.json").exists()) {
    return
  }

  with(pluginManager) {
    apply("com.github.triplet.play")
  }

  val versionCode = applicationExtension.defaultConfig.versionCode
  val versionName = applicationExtension.defaultConfig.versionName

  applicationExtension.apply {
    (this as ExtensionAware)
      .extensions
      .configure<NamedDomainObjectContainer<PlayPublisherExtension>>("playConfigs") {
        register("googlePlayRelease") {
          enabled.set(true)
          releaseName.set("$versionCode ($versionName)")
          track.set("internal")
        }
      }
  }
}
