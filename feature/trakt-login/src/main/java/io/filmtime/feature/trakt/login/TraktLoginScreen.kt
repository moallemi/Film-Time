package io.filmtime.feature.trakt.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import io.filmtime.core.designsystem.composable.FilmTimeFilledButton
import io.filmtime.core.designsystem.composable.FilmTimeSmallTopAppBar
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews

@Composable
fun TraktLoginScreen(
  onBackClick: () -> Unit,
) {
  val viewModel = hiltViewModel<TraktLoginViewModel>()
  val loginState by viewModel.loginState.collectAsStateWithLifecycle()
  val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

  LaunchedEffect(isLoggedIn) {
    if (isLoggedIn) {
      onBackClick()
    }
  }

  TraktLoginScreen(
    loginState = loginState,
    onBackClick = onBackClick,
  )
}

@Composable
private fun TraktLoginScreen(
  loginState: LoginState,
  onBackClick: () -> Unit,
) {
  Scaffold(
    topBar = {
      FilmTimeSmallTopAppBar(
        title = "Trakt Login",
      )
    },
  ) { padding ->
    Column(
      modifier = Modifier
        .fillMaxSize()
        .padding(padding),
      verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
      horizontalAlignment = Alignment.CenterHorizontally,
    ) {
      when (loginState) {
        LoginState.Loading -> {
          Text(text = "Logging in to Trakt...")
          CircularProgressIndicator()
        }

        LoginState.Success -> {
          Text(text = "You are now logged in to Trakt. You can sync your watchlist now.")
        }

        LoginState.Failed -> {
          Text(text = "Could not login to Trakt. Please try again.")
          FilmTimeFilledButton(onClick = onBackClick) {
            Text(text = "Close")
          }
        }
      }
    }
  }
}

@ThemePreviews
@Composable
fun TraktLoginScreenPreview() {
  PreviewFilmTimeTheme {
    TraktLoginScreen(
      loginState = LoginState.Loading,
      onBackClick = {},
    )
  }
}
