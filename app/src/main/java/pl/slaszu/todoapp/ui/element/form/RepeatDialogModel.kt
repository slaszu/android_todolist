package pl.slaszu.todoapp.ui.element.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.repeat.RepeatType

@Composable
fun RepeatDialogModel(
    selectedType: RepeatType?,
    onDismiss: () -> Unit,
    onClick: (RepeatType?) -> Unit
) {
    var type by remember { mutableStateOf(selectedType) }

    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = { onDismiss() }) {
                Text(stringResource(R.string.btn_back))
            }
        },
        confirmButton = {
            TextButton(onClick = {
                onClick(type)
                onDismiss()
            }) {
                Text("OK")
            }
        },
        text = {
            LazyColumn {
                items(
                    items = RepeatType.getAll()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                type = it
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (it.period == type?.period),
                            onClick = {
                                type = it
                            }
                        )
                        Text(
                            text = stringResource(it.translationKey ?: 0)
                        )
                    }

                    HorizontalDivider()
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                type = null
                            },
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = false,
                                    onClick = {
                                        type = null
                                    }
                                )
                                Text(
                                    text = "other"
                                )
                            }
                            Row {
                                DropdownExample(initialState = false)
                            }
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropdownExample(initialState: Boolean) {

    val periods = listOf(
        Pair("D", "days"),
        Pair("W", "weeks"),
        Pair("M", "months"),
        Pair("Y", "years")
    )

    var expanded by remember { mutableStateOf(false) }
    var digit by remember { mutableStateOf(2) }
    var text by remember { mutableStateOf(periods[0].first) }

    var periodChoose by remember { mutableStateOf(periods[0].second) }


    Row {
        // digit
        OutlinedTextField(
            value = digit.toString(),
            onValueChange = {},
            //readOnly = true,
            singleLine = true,
            label = { Text("digit") },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.weight(0.3f)
        )


        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.weight(0.7f)
        ) {
            OutlinedTextField(
                // The `menuAnchor` modifier must be passed to the text field to handle
                // expanding/collapsing the menu on click. A read-only text field has
                // the anchor type `PrimaryNotEditable`.
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable),
                value = text,
                onValueChange = {},
                singleLine = true,
                label = { Text("period") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                periods.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option.second, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            text = option.second
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    )
                }
            }
        }

    }

}


@Composable
@Preview
fun RepeatDialogModalPreview() {
    RepeatDialogModel(
        selectedType = null,
        onDismiss = { },
        onClick = { }
    )
}