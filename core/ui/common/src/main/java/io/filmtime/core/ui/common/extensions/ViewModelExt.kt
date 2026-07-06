package io.filmtime.core.ui.common.extensions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

fun ViewModel.launch(block: suspend CoroutineScope.() -> Unit): Job =
  viewModelScope.launch { block() }
