package io.filmtime.core.ui.common.componnents

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import io.filmtime.core.designsystem.theme.PreviewFilmTimeTheme
import io.filmtime.core.designsystem.theme.ThemePreviews

@Composable
fun <T> Select(
  modifier: Modifier = Modifier,
  options: List<T>,
  itemText: (T) -> String,
  selectedItem: T,
  label: String? = null,
  onSelect: (T) -> Unit,
) {
  var expanded by remember { mutableStateOf(false) }

  ExposedDropdownMenuBox(
    modifier = modifier,
    expanded = expanded,
    onExpandedChange = { expanded = !expanded },
  ) {
    SelectTextField(
      modifier = Modifier.menuAnchor(),
      value = itemText(selectedItem),
      onValueChange = {},
      shape = MaterialTheme.shapes.medium,
      trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
    )
    ExposedDropdownMenu(
      expanded = expanded,
      onDismissRequest = { expanded = false },
    ) {
      options.forEach { selectionOption ->
        DropdownMenuItem(
          text = { Text(itemText(selectionOption)) },
          onClick = {
            onSelect(selectionOption)
            expanded = false
          },
        )
      }
    }
  }
}

@Composable
@ThemePreviews
private fun PreviewSelect() {
  PreviewFilmTimeTheme {
    Select(
      label = "Label",
      options = listOf("Option 1", "Option 2", "Option 3"),
      itemText = { it },
      selectedItem = "Option 1",
      onSelect = {},
    )
  }
}
