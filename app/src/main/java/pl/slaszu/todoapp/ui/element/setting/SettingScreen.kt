package pl.slaszu.todoapp.ui.element.setting

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.ui.element.form.TimeDialogModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    setting: Setting,
    onChange: (Setting) -> Unit,
    onNotificationClick: () -> Unit,
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val timePickerState = rememberTimePickerState(
        initialHour = setting.reminderRepeatHour,
        initialMinute = setting.reminderRepeatMinute,
        is24Hour = true,
    )

    var chooseTimeDialog by remember { mutableStateOf(false) }
    if (chooseTimeDialog) {
        TimeDialogModel(
            timePickerState = timePickerState,
            onDismiss = { chooseTimeDialog = false },
            onConfirm = {
                onChange(
                    setting.copy(
                        reminderRepeatHour = it.hour,
                        reminderRepeatMinute = it.minute
                    )
                )
            }
        )
    }

    Column(modifier = Modifier.padding(10.dp)) {

        SettingOption(
            text = "Pokazać elementy zakończone ?",
            description = "Będą one pokazywane na liscie zadań"
        ) {
            Switch(
                checked = setting.showDone,
                onCheckedChange = { change ->
                    onChange(setting.copy(showDone = change))
                }
            )
        }

        SettingOption(
            text = "Ogólne dzienne powiadomienie",
            description = "O ktrórej godzienie ma się pojawiać"
        ) {
            Button(
                onClick = { chooseTimeDialog = true }
            ) {
                Text(
                    text = "${setting.reminderRepeatHour}:${
                        setting.reminderRepeatMinute.toString().padStart(2, '0')
                    }"
                )
            }
        }

        SettingOption(
            text = "Powiadomienia",
            description = "Wyświetlanie powiadomień o zadaniach"
        ) {

            Switch(
                checked = setting.notificationAllowed,
                onCheckedChange = { onNotificationClick() }
            )
        }


        SettingOption(
            text = "Powiadomienia",
            description = "Ustawianie powiadomień w systemie"
        ) {

            Switch(
                checked = setting.reminderAllowed,
                onCheckedChange = { onReminderClick() }
            )
        }
    }
}

@Composable
private fun SettingOption(
    text: String,
    description: String,
    content: @Composable () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp)
    ) {

        Column(
            modifier = Modifier.weight(0.9f)
        ) {
            Text(
                text = text,
                fontSize = TextUnit(4f, TextUnitType.Em),

                )
            Text(
                text = description,
                fontSize = TextUnit(3f, TextUnitType.Em),
            )
        }
        content()
    }
}

@Preview
@Composable
fun TodoListSettingPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            SettingScreen(
                setting = Setting(true, false, reminderAllowed = false),
                onChange = {},
                onNotificationClick = {},
                onReminderClick = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}