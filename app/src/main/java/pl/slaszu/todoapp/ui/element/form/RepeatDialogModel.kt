package pl.slaszu.todoapp.ui.element.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
        shape = RoundedCornerShape(28.dp),
        confirmButton = {
            TextButton(onClick = {
                onClick(type)
                onDismiss()
            }) {
                Text("OK", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.btn_back), color = MaterialTheme.colorScheme.secondary)
            }
        },
        title = {
            Text(
                text = "Powtarzanie zadania",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                // Predefiniowane opcje
                items(items = RepeatType.getAll()) { item ->
                    RepeatOptionRow(
                        label = item.getTranslation(resource = LocalResources.current),
                        selected = (item.period == type?.period && type !is RepeatTypeOther),
                        onClick = { type = item }
                    )
                }

                // Opcja "Inne" (Twój Wizard)
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (type is RepeatTypeOther) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
                                else Color.Transparent
                            )
                            .padding(bottom = 8.dp)
                    ) {
                        RepeatOptionRow(
                            label = stringResource(R.string.other),
                            selected = type is RepeatTypeOther,
                            onClick = { type = getOtherType(type) }
                        )

                        if (type is RepeatTypeOther) {
                            Box(modifier = Modifier.padding(horizontal = 12.dp)) {
                                PeriodWizard(
                                    initialPeriod = (type as RepeatTypeOther).period,
                                    onChange = { type = RepeatTypeOther(it) }
                                )
                            }
                        }
                    }
                }
            }
        }
    )
}

@Composable
private fun RepeatOptionRow(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        color = if (selected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f) else Color.Transparent,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = selected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
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

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = digit,
            onValueChange = {
                digit = it.replace(Regex("[^0-9]"), "").toIntOrNull()?.toString() ?: ""
                onChange(createPeriodFromTypeAndCount(periodChoose.first, digit.toIntOrNull() ?: 1))
            },
            label = { Text(stringResource(R.string.quantity)) },
            singleLine = true,
            modifier = Modifier.weight(0.4f).onFocusChanged {
                if (!it.isFocused && digit.isEmpty()) digit = "1"
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            shape = RoundedCornerShape(12.dp)
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it },
            modifier = Modifier.weight(0.6f)
        ) {
            OutlinedTextField(
                modifier = Modifier.menuAnchor(),
                value = periodChoose.second,
                onValueChange = {},
                readOnly = true,
                label = { Text(stringResource(R.string.type)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                periods.forEach { period ->
                    DropdownMenuItem(
                        text = { Text(period.second) },
                        onClick = {
                            periodChoose = period
                            expanded = false
                            onChange(createPeriodFromTypeAndCount(period.first, digit.toIntOrNull() ?: 1))
                        }
                    )
                }
            }
        }
    }
}

private fun getOtherType(type: RepeatType?): RepeatTypeOther =
    if (type is RepeatTypeOther) type else RepeatTypeOther(Period.ofDays(2))