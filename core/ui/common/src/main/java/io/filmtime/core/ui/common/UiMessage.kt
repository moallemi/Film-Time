package io.filmtime.core.ui.common

import androidx.annotation.PluralsRes
import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import io.filmtime.data.model.GeneralError
import io.filmtime.data.model.GeneralError.ApiError
import io.filmtime.data.model.GeneralError.NetworkError
import io.filmtime.data.model.GeneralError.UnknownError
import io.filmtime.data.model.GeneralErrorThrowable
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.util.UUID

sealed class UiMessage(
  @RawRes open val animationResource: Int? = null,
  open val id: String,
) {
  data class Text(
    val text: String,
    @RawRes override val animationResource: Int? = null,
    override val id: String = UUID.randomUUID().toString(),
  ) : UiMessage(animationResource, id)

  data class Res(
    @StringRes val textId: Int,
    @RawRes override val animationResource: Int? = null,
    val formatArgs: List<Any> = emptyList(),
    override val id: String = UUID.randomUUID().toString(),
  ) : UiMessage(animationResource, id)

  data class PluralRes(
    @PluralsRes val pluralId: Int,
    val formatArgs: List<Any> = emptyList(),
    @RawRes override val animationResource: Int? = null,
    override val id: String = UUID.randomUUID().toString(),
  ) : UiMessage(animationResource, id)

  @Composable
  fun string(): String {
    val context = LocalContext.current
    return when (this) {
      is Text -> text
      is Res -> context.resources.getString(textId, *formatArgs.toTypedArray())
      is PluralRes -> context.resources.getQuantityString(
        pluralId,
        formatArgs.first() as Int,
        *formatArgs.toTypedArray(),
      )
    }
  }
}

fun UiMessage(
  throwable: Throwable,
  id: String = UUID.randomUUID().toString(),
): UiMessage = when (throwable) {
  is GeneralErrorThrowable -> throwable.generalError.toUiMessage()
  else -> UiMessage.Text(text = throwable.message ?: "Error occurred: ${throwable.stackTraceToString()}", id = id)
}

class UiMessageManager {
  private val mutex = Mutex()

  private val messages = MutableStateFlow(emptyList<UiMessage>())

  val message: Flow<UiMessage?> = messages.map { it.firstOrNull() }.distinctUntilChanged()

  suspend fun emitMessage(message: UiMessage) {
    mutex.withLock {
      messages.value += message
    }
  }

  suspend fun clearAll() {
    mutex.withLock {
      messages.value = emptyList()
    }
  }

  suspend fun clearMessage(id: String) {
    mutex.withLock {
      messages.value = messages.value.filterNot { it.id == id }
    }
  }
}

fun GeneralError.toUiMessage(): UiMessage = when (this) {
  is ApiError -> message?.let { message ->
    UiMessage.Text(text = message, animationResource = R.raw.error)
  } ?: UiMessage.Res(textId = R.string.unknown_api_error, animationResource = R.raw.error)

  NetworkError -> UiMessage.Res(textId = R.string.check_internet_connection, animationResource = R.raw.network_lost)
  is UnknownError -> error.message?.let { message ->
    UiMessage.Text(text = message, animationResource = R.raw.error)
  } ?: UiMessage.Res(R.string.unknown_error, R.raw.error)
}
