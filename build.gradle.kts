// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
  alias(libs.plugins.com.android.application) apply false
  alias(libs.plugins.org.jetbrains.kotlin.android) apply false
  alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
  alias(libs.plugins.com.android.library) apply false
  alias(libs.plugins.kotlinx.serialization) apply false

  id("com.diffplug.spotless") version "6.18.0"
  alias(libs.plugins.hilt.android) apply false
}
true // Needed to make the Suppress annotation work for the plugins block

spotless {
  kotlin {
    target("**/*.kt", "**/*.kts")
    targetExclude("$buildDir/**/*.kt", "bin/**/*.kt", "buildSrc/**/*.kt")

    ktlint()
  }
}

val installGitHook by tasks.registering(Copy::class) {
  from(file("${rootProject.rootDir}/.scripts/pre-commit"))
  into(file("${rootProject.rootDir}/.git/hooks"))
  fileMode = 0b111101101
}

project(":app").afterEvaluate {
  tasks.named("preBuild").configure {
    dependsOn(":installGitHook")
  }
}
