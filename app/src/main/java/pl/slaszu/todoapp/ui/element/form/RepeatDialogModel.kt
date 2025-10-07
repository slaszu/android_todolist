package pl.slaszu.todoapp.ui.element.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.repeat.RepeatType
import pl.slaszu.todoapp.domain.utils.createPeriodFromTypeAndCount
import pl.slaszu.todoapp.domain.utils.getTypeAndCount
import java.time.Period

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
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        PeriodRadioButton(
                            type = type,
                            onSelect = {
                                type = it
                            }
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun PeriodRadioButton(
    type: RepeatType?,
    onSelect: (RepeatType?) -> Unit
) {
    var selected = false
    var otherType by remember { mutableStateOf(RepeatType.toObject("P2D")) }
    if (type is RepeatType.RepeatTypeOther) {
        selected = true
        otherType = RepeatType.toObject(type.toStringRepresentation())
    }

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = { onSelect(otherType) }
            )
            Text(
                text = "other"
            )
        }
        Row {
            PeriodWizard(
                initialPeriod = otherType.period,
                onChange = {
                    otherType = RepeatType.RepeatTypeOther(it)
                    onSelect(otherType)
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PeriodWizard(
    initialPeriod: Period,
    onChange: (Period) -> Unit
) {

    val periods = listOf(
        Pair("D", "days"),
        Pair("W", "weeks"),
        Pair("M", "months"),
        Pair("Y", "years")
    )

    val periodData = initialPeriod.getTypeAndCount()

    var expanded by remember { mutableStateOf(false) }
    var digit by remember { mutableStateOf(periodData.second.toString()) }

    var periodChoose by remember {
        mutableStateOf(periods.find { it.first == periodData.first } ?: periods.first())
    }

    fun changeAction() {
        onChange(
            createPeriodFromTypeAndCount(
                type = periodChoose.first,
                count = digit.takeIf { it.isNotEmpty() }?.toInt() ?: 1
            )
        )
    }

    Row {
        // digit
        OutlinedTextField(
            value = digit,
            onValueChange = {
                digit = it
                changeAction()
            },
            label = { Text("digit") },
            singleLine = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier.weight(0.3f),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
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
                value = periodChoose.second,
                onValueChange = {},
                singleLine = true,
                readOnly = true,
                label = { Text("period") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                periods.forEach { period ->
                    DropdownMenuItem(
                        text = { Text(period.second, style = MaterialTheme.typography.bodyLarge) },
                        onClick = {
                            periodChoose = period
                            expanded = false
                            changeAction()
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