package io.filmtime.gradle

import com.android.build.api.dsl.ApplicationExtension
import me.moallemi.gradle.advancedbuildversion.gradleextensions.AdvancedBuildVersionConfig
import me.moallemi.gradle.advancedbuildversion.gradleextensions.VersionCodeType.GIT_COMMIT_COUNT
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureVersionCode(
  applicationExtension: ApplicationExtension,
) {
  configure<AdvancedBuildVersionConfig> {
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

  applicationExtension.apply {
    val versioning = extensions.getByName("advancedVersioning") as AdvancedBuildVersionConfig
    this.defaultConfig.versionCode = versioning.versionCode
    this.defaultConfig.versionName = versioning.versionName
  }
}
