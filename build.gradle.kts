plugins {
  alias(libs.plugins.com.android.application) apply false
  alias(libs.plugins.org.jetbrains.kotlin.android) apply false
  alias(libs.plugins.org.jetbrains.kotlin.jvm) apply false
  alias(libs.plugins.com.android.library) apply false
  alias(libs.plugins.kotlinx.serialization) apply false
  alias(libs.plugins.ksp) apply false

  alias(libs.plugins.spotless)
  alias(libs.plugins.hilt.android) apply false
}

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
