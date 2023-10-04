package io.filmtime.feature.trakt.login

import android.view.ViewGroup
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

private const val TRAKT_LOGIN_URL =
  "https://api.trakt.tv/oauth/authorize?response_type=code&client_id=${BuildConfig.TRAKT_CLIENT_ID}&redirect_uri=filmtime://"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TraktLoginWebView(
  onBackPressed: () -> Unit,
  onSuccess: (String) -> Unit,
) {


  Scaffold(
    topBar = {
      TopAppBar(
        title = {},
        navigationIcon = {
          IconButton(onClick = onBackPressed) {
            Icon(Icons.Default.ArrowBack, contentDescription = "back")
          }
        },
      )
    },
  ) {
    Box(
      modifier = Modifier.padding(it),
    ) {
      AndroidView(
        factory = {
          WebView(it).apply {
            layoutParams = ViewGroup.LayoutParams(
              ViewGroup.LayoutParams.MATCH_PARENT,
              ViewGroup.LayoutParams.MATCH_PARENT,
            )
            webViewClient = object : WebViewClient() {
              override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {

                request?.url?.let { uri ->
                  if (uri.scheme == "filmtime") {
                    onSuccess(uri.getQueryParameter("code")!!)
                  }
                }
                return super.shouldOverrideUrlLoading(view, request)
              }

            }
            loadUrl(TRAKT_LOGIN_URL)

          }
        },
        update = {
          it.loadUrl(TRAKT_LOGIN_URL)
        },
      )
    }
  }
}
