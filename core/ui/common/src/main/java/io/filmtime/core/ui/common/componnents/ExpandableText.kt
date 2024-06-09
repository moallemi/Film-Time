package io.filmtime.core.ui.common.componnents

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews
import java.util.regex.Pattern

/**
 * From https://github.com/ElementalistBTG/Ninja-wallet/blob/main/app/src/main/java/com/elementalist/ninjawallet/common/useful/ExpandingText.kt
 */
@Composable
fun ExpandableText(
  modifier: Modifier = Modifier,
  text: String,
  textStyle: TextStyle = MaterialTheme.typography.bodyMedium.copy(
    color = MaterialTheme.colorScheme.onSurface,
  ),
  minimizedMaxLines: Int,
) {
  var isExpanded by rememberSaveable { mutableStateOf(false) }
  val textLayoutResultState = remember { mutableStateOf<TextLayoutResult?>(null) }
  var isClickable by rememberSaveable { mutableStateOf(false) }
  val textLayoutResult = textLayoutResultState.value

  // first we match the html tags and enable the links
  val textWithLinks = buildAnnotatedString {
    val htmlTagPattern = Pattern.compile(
      "(?i)<a([^>]+)>(.+?)</a>",
      Pattern.CASE_INSENSITIVE or Pattern.MULTILINE or Pattern.DOTALL,
    )
    val matcher = htmlTagPattern.matcher(text)
    var matchStart: Int
    var matchEnd = 0
    var previousMatchStart = 0

    while (matcher.find()) {
      matchStart = matcher.start(1)
      matchEnd = matcher.end()
      val beforeMatch = text.substring(
        startIndex = previousMatchStart,
        endIndex = matchStart - 2,
      )
      val tagMatch = text.substring(
        startIndex = text.indexOf(
          char = '>',
          startIndex = matchStart,
        ) + 1,
        endIndex = text.indexOf(
          char = '<',
          startIndex = matchStart + 1,
        ),
      )
      append(
        beforeMatch,
      )
      // attach a string annotation that stores a URL to the text
      val annotation = text.substring(
        startIndex = matchStart + 7, // omit <a href =
        endIndex = text.indexOf(
          char = '"',
          startIndex = matchStart + 7,
        ),
      )
      pushStringAnnotation(tag = "link_tag", annotation = annotation)
      withStyle(
        SpanStyle(
          color = MaterialTheme.colorScheme.primary,
          textDecoration = TextDecoration.Underline,
        ),
      ) {
        append(
          tagMatch,
        )
      }
      pop() // don't forget to add this line after a pushStringAnnotation
      previousMatchStart = matchEnd
    }
    // append the rest of the string
    if (text.length > matchEnd) {
      append(
        text.substring(
          startIndex = matchEnd,
          endIndex = text.length,
        ),
      )
    }
  }
  // then we create the Show more/less animation effect
  var textWithMoreLess by remember { mutableStateOf(textWithLinks) }
  val buttonColor = MaterialTheme.colorScheme.primary
  val buttonSpanStyle = SpanStyle(
    color = buttonColor,
    fontSize = MaterialTheme.typography.labelLarge.fontSize,
  )

  LaunchedEffect(textLayoutResult) {
    if (textLayoutResult == null) return@LaunchedEffect

    when {
      isExpanded -> {
        textWithMoreLess = buildAnnotatedString {
          append(textWithLinks)
          pushStringAnnotation(tag = "show_more_tag", annotation = "")
          withStyle(buttonSpanStyle) {
            append(" LESS")
          }
          pop()
        }
      }

      !isExpanded && textLayoutResult.hasVisualOverflow -> {
        val lastCharIndex = textLayoutResult.getLineEnd(minimizedMaxLines - 1)
        val showMoreString = "… MORE"
        val adjustedText = textWithLinks
          .substring(startIndex = 0, endIndex = lastCharIndex)
          .dropLast(showMoreString.length)
          .dropLastWhile { it == ' ' || it == '.' }

        textWithMoreLess = buildAnnotatedString {
          append(adjustedText)
          pushStringAnnotation(tag = "show_more_tag", annotation = "")
          withStyle(buttonSpanStyle) {
            append(showMoreString)
          }
          pop()
        }

        isClickable = true
        // We basically need to assign this here so that the Text is only clickable if the state is not expanded,
        // but there is visual overflow. Otherwise, it means that the text given to the composable is not exceeding the max lines.
      }
    }
  }

  // UriHandler parse and opens URI inside AnnotatedString Item in Browse
  val uriHandler = LocalUriHandler.current

  // Composable container
  SelectionContainer {
    ClickableText(
      text = textWithMoreLess,
      style = textStyle,
      onClick = { offset ->
        textWithMoreLess.getStringAnnotations(
          tag = "link_tag",
          start = offset,
          end = offset,
        ).firstOrNull()?.let { stringAnnotation ->
          uriHandler.openUri(stringAnnotation.item)
        }
        if (isClickable) {
          textWithMoreLess.getStringAnnotations(
            tag = "show_more_tag",
            start = offset,
            end = offset,
          ).firstOrNull()?.let {
            isExpanded = !isExpanded
          }
        }
      },
      maxLines = if (isExpanded) Int.MAX_VALUE else minimizedMaxLines,
      onTextLayout = { textLayoutResultState.value = it },
      modifier = modifier
        .animateContentSize(),
    )
  }
}

@ThemePreviews
@Composable
private fun ExpandableTextPreview() {
  PreviewFilmTimeTheme {
    ExpandableText(
      text = "This is a long text that will <a href=\"https://google.com\">Click Here</a> be cut off after 3 lines." +
        " So I think is would be better to show more text when the user clicks on the Show More button.",
      minimizedMaxLines = 1,
    )
  }
}
