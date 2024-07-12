pluginManagement {
  includeBuild("gradle/build-logic")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}

rootProject.name = "Film Time"
include(":app")
include(":data:network")
include(":data:model")
include(":data:api:tmdb")
include(":data:tmdb-movies")
include(":domain:tmdb-movies")
include(":feature:movie-detail")
include(":core:libs:logger")
include(":core:libs:tracker")
include(":feature:home")
include(":domain:stream")
include(":feature:player")
include(":data:tmdb-shows")
include(":domain:tmdb-shows")
include(":feature:show-detail")
include(":feature:trakt-login")
include(":data:api:trakt")
include(":domain:trakt:auth")
include(":data:trakt-auth")
include(":data:storage:trakt")
include(":domain:trakt:history")
include(":data:trakt")
include(":feature:video-thumbnail-grid")
include(":core:ui:common")
include(":core:ui:navigation")
include(":core:design-system")
include(":feature:movies")
include(":feature:shows")
include(":feature:settings")
include(":data:database")
include(":data:bookmarks")
include(":domain:bookmarks")
include(":core:browser")
include(":feature:trakt-buttons")
include(":feature:credits")
include(":feature:similar")
include(":domain:trakt:trakt")
