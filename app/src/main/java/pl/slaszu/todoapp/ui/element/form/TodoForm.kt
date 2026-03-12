package pl.slaszu.todoapp.ui.element.form

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Event
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.utils.clearTime
import pl.slaszu.todoapp.domain.utils.isTimeSet
import pl.slaszu.todoapp.domain.utils.printDate
import pl.slaszu.todoapp.domain.utils.printTime
import pl.slaszu.todoapp.domain.utils.setTime
import pl.slaszu.todoapp.domain.utils.toEpochMillis
import pl.slaszu.todoapp.domain.utils.toLocalDateTime
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodoForm(
    item: TodoModel?,
    onSave: (TodoModel) -> Unit,
    modifier: Modifier = Modifier
) {
    if (item == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    val currentTime = LocalDateTime.now()
    var text by remember { mutableStateOf(item.text) }
    var todoLocalDateTime by rememberSaveable { mutableStateOf(item.startDate) }
    var repeatType by remember { mutableStateOf(item.repeatType) }

    var chooseDateDialog by remember { mutableStateOf(false) }
    var chooseTimeDialog by remember { mutableStateOf(false) }
    var showRepeatOptions by remember { mutableStateOf(false) }

    // --- STANY DLA MATERIAL 3 PICKERS (z walidacją daty) ---
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = todoLocalDateTime?.toEpochMillis(),
        selectableDates = object : SelectableDates {
            override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                // Pozwalaj na wybór tylko dat >= dzisiaj (północ)
                val todayStartMillis = LocalDateTime.now().clearTime().toEpochMillis()
                return utcTimeMillis >= todayStartMillis
            }

            override fun isSelectableYear(year: Int): Boolean {
                // Opcjonalnie: nie pozwalaj na wybór lat z przeszłości
                return year >= LocalDateTime.now().year
            }
        }
    )

    val timePickerState = rememberTimePickerState(
        initialHour = todoLocalDateTime?.hour ?: (currentTime.hour + 1),
        initialMinute = todoLocalDateTime?.minute ?: 0,
        is24Hour = true
    )

    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- SEKCJA TEKSTU ---
        OutlinedTextField(
            value = text,
            onValueChange = { text = it },
            label = { Text(stringResource(R.string.todo_form_text_label)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .focusRequester(focusRequester),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
            )
        )

        Text(
            text = "Szybki termin",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp), // Odstęp między wierszami
            maxItemsInEachRow = Int.MAX_VALUE // Pozwól na dowolną liczbę elementów w wierszu
        ) {
            // 1. Za godzinę
            QuickDateChip("Za godzinę", isSelected = false) {
                todoLocalDateTime = LocalDateTime.now().plusHours(1)
            }

            // 2. Jutro
            QuickDateChip("Jutro", isSelected = false) {
                todoLocalDateTime = LocalDateTime.now().plusDays(1).clearTime()
            }

            // 3. Za tydzień
            QuickDateChip("Za tydzień", isSelected = false) {
                todoLocalDateTime = LocalDateTime.now().plusWeeks(1).clearTime()
            }

            // 4. Za miesiąc
            QuickDateChip("Za miesiąc", isSelected = false) {
                todoLocalDateTime = LocalDateTime.now().plusMonths(1).clearTime()
            }

            // Przycisk wyczyść
            if (todoLocalDateTime != null) {
                AssistChip(
                    onClick = {
                        todoLocalDateTime = null
                        repeatType = null
                    },
                    label = { Text("Wyczyść") },
                    leadingIcon = { Icon(Icons.Default.Clear, null, Modifier.size(16.dp)) },
                    shape = RoundedCornerShape(12.dp),
                    colors = AssistChipDefaults.assistChipColors(
                        labelColor = MaterialTheme.colorScheme.error
                    )
                )
            }
        }

        Text(
            text = "Szczegóły przypomnienia",
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        // --- SEKCJA KONFIGURACJI ---
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f))
        ) {
            FormRow(
                icon = Icons.Rounded.Event,
                label = "Data",
                value = todoLocalDateTime?.printDate() ?: "Nie ustawiono",
                onClick = { chooseDateDialog = true },
                onClear = if (todoLocalDateTime != null) {
                    { todoLocalDateTime = null }
                } else null
            )

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)

            AnimatedVisibility(visible = todoLocalDateTime != null) {
                FormRow(
                    icon = Icons.Rounded.Schedule,
                    label = "Godzina",
                    value = todoLocalDateTime?.printTime() ?: "Wybierz godzinę",
                    onClick = { chooseTimeDialog = true },
                    onClear = if (todoLocalDateTime?.isTimeSet() == true) {
                        { todoLocalDateTime = todoLocalDateTime?.clearTime() }
                    } else null
                )
            }

            HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)

            AnimatedVisibility(visible = todoLocalDateTime != null) {
                FormRow(
                    icon = Icons.Rounded.Update,
                    label = "Powtarzanie",
                    value = repeatType?.getTranslation(LocalResources.current) ?: "Brak",
                    onClick = { showRepeatOptions = true },
                    onClear = if (repeatType != null) {
                        { repeatType = null }
                    } else null
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            enabled = text.isNotBlank(),
            onClick = {
                onSave(
                    item.copy(
                        "text" to text,
                        "startDate" to todoLocalDateTime,
                        "repeatType" to repeatType
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
        ) {
            Icon(Icons.Default.Check, null)
            Spacer(Modifier.width(8.dp))
            Text(
                stringResource(R.string.todo_form_save_btn),
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }
    }

    // --- WYWOŁANIA MODALI (To tutaj przywraca działanie dialogów) ---

    if (chooseDateDialog) {
        DatePickerModal(
            datePickerState = datePickerState,
            onDismiss = { chooseDateDialog = false },
            onDateSelected = { millis ->
                todoLocalDateTime = millis?.toLocalDateTime()?.clearTime()
                chooseDateDialog = false
            }
        )
    }

    if (chooseTimeDialog) {
        TimeDialogModel(
            timePickerState = timePickerState,
            onDismiss = { chooseTimeDialog = false },
            onConfirm = { time ->
                todoLocalDateTime = todoLocalDateTime?.setTime(time.hour, time.minute)
                chooseTimeDialog = false
            }
        )
    }

    if (showRepeatOptions) {
        RepeatDialogModel(
            selectedType = repeatType,
            onDismiss = { showRepeatOptions = false },
            onClick = { selectedType ->
                repeatType = selectedType
                showRepeatOptions = false
            }
        )
    }
}

@Composable
fun QuickDateChip(label: String, isSelected: Boolean, onClick: () -> Unit) {
    FilterChip(
        selected = isSelected,
        onClick = onClick,
        label = { Text(label) },
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun FormRow(
    icon: ImageVector,
    label: String,
    value: String,
    onClick: () -> Unit,
    onClear: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(value, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Medium)
        }
        if (onClear != null) {
            IconButton(onClick = onClear) {
                Icon(Icons.Default.Close, null, modifier = Modifier.size(18.dp))
            }
        } else {
            Icon(Icons.Default.ChevronRight, null, tint = MaterialTheme.colorScheme.outline)
        }
    }
}