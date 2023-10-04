package io.filmtime.feature.trakt.login

import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView

private const val TRAKT_LOGIN_URL = "https://api.trakt.tv/oauth/authorize"

@Composable
fun TraktLoginWebView() {

  AndroidView(factory = {
    WebView(it).apply {
      layoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
      webViewClient = WebViewClient()
      loadUrl(TRAKT_LOGIN_URL)
    }
  }, update = {
    it.loadUrl(TRAKT_LOGIN_URL)
  })
}
