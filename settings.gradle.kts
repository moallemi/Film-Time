pluginManagement {
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
