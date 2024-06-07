package io.filmtime.gradle

import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.provideDelegate
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinAndroidProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmProjectExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinTopLevelExtension

internal fun Project.configureKotlinAndroid() {
  extensions.configure<BaseExtension> {
    compileSdkVersion(Versions.COMPILE_SDK)

    defaultConfig {
      minSdk = Versions.MIN_SDK
      targetSdk = Versions.TARGET_SDK
      testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
      sourceCompatibility = JavaVersion.VERSION_11
      targetCompatibility = JavaVersion.VERSION_11
    }
  }

  configureKotlin<KotlinAndroidProjectExtension>()
}

/**
 * Configure base Kotlin options for JVM (non-Android)
 */
internal fun Project.configureKotlinJvm() {
  extensions.configure<JavaPluginExtension> {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
  }

  configureKotlin<KotlinJvmProjectExtension>()
}

/**
 * Configure base Kotlin options
 */
private inline fun <reified T : KotlinTopLevelExtension> Project.configureKotlin() = configure<T> {
  val warningsAsErrors: String? by project
  when (this) {
    is KotlinAndroidProjectExtension -> compilerOptions
    is KotlinJvmProjectExtension -> compilerOptions
    else -> throw NotImplementedError("Unsupported project extension $this ${T::class}")
  }.apply {
    jvmTarget.set(JvmTarget.JVM_11)

    freeCompilerArgs.addAll(
      "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
      "-opt-in=coil.annotation.ExperimentalCoilApi",
      "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
    )
  }
}
