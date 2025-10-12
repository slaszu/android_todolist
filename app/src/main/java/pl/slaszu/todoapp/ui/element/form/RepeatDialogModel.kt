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
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.repeat.RepeatType
import pl.slaszu.todoapp.domain.repeat.RepeatTypeOther
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
                            selected = (it.period == type?.period && type !is RepeatTypeOther),
                            onClick = {
                                type = it
                            }
                        )
                        Text(
                            text = it.getTranslation(resource = LocalResources.current)
                        )
                    }

                    HorizontalDivider()
                }
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                type = getOtherType(type)
                            },
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

private fun getOtherType(type: RepeatType?): RepeatTypeOther {
    if (type is RepeatTypeOther) {
        return type
    }
    return RepeatTypeOther(Period.parse("P2D"))
}

@Composable
fun PeriodRadioButton(
    type: RepeatType?,
    onSelect: (RepeatType?) -> Unit
) {
    var otherType = getOtherType(type)

    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = type is RepeatTypeOther,
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
                    onSelect(RepeatTypeOther(it))
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
        Pair("D", stringResource(R.string.days)),
        Pair("W", stringResource(R.string.weeks)),
        Pair("M", stringResource(R.string.months)),
        Pair("Y", stringResource(R.string.years))
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
                digit = it.replace(Regex("[^0-9]"), "").toIntOrNull()?.toString() ?: ""
                changeAction()
            },
            label = { Text(stringResource(R.string.quantity)) },
            singleLine = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            modifier = Modifier
                .weight(0.3f)
                .onFocusChanged(
                    onFocusChanged = {
                        if (!it.isFocused && digit.isEmpty()) {
                            digit = "1"
                        }
                    }
                ),
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
                label = { Text(stringResource(R.string.type)) },
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
                            expanded = false
                            periodChoose = period
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