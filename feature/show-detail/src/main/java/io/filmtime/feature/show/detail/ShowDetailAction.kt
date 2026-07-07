package io.filmtime.feature.show.detail

import io.filmtime.core.plugin.api.PluginMetadata
import io.filmtime.data.model.EpisodeThumbnail

internal sealed interface ShowDetailAction {
  data object Reload : ShowDetailAction
  data object AddBookmark : ShowDetailAction
  data object RemoveBookmark : ShowDetailAction
  data class ChangeSeason(val seasonNumber: Int) : ShowDetailAction
  data class AddEpisodeToHistory(val episode: EpisodeThumbnail) : ShowDetailAction
  data class RemoveEpisodeFromHistory(val episode: EpisodeThumbnail) : ShowDetailAction
  data class PlayEpisode(val episode: EpisodeThumbnail) : ShowDetailAction
  data class SelectPlugin(val plugin: PluginMetadata) : ShowDetailAction
  data object DismissPluginSelection : ShowDetailAction
  data object DismissNoPluginsDialog : ShowDetailAction
  data class PluginLoginResult(val success: Boolean) : ShowDetailAction
}
