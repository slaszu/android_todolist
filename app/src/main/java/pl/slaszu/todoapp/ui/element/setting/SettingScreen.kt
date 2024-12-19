package pl.slaszu.todoapp.ui.element.setting

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun SettingScreen(
    setting: Setting,
    onChange: (Setting) -> Unit,
    onNotificationClick: () -> Unit,
    onReminderClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier.padding(10.dp)) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(
                text = stringResource(R.string.setting_show_done),
                modifier = Modifier.padding(10.dp, 0.dp)
            )
            Switch(
                checked = setting.showDone,
                onCheckedChange = { change ->
                    onChange(setting.copy(showDone = change))
                }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {

            if (setting.notificationAllowed) {
                Text(
                    text = "Notifications allowed",
                    color = Color.Gray
                )
            } else {
                Text(
                    text = "Notification not allowed",
                    modifier = Modifier.padding(10.dp, 0.dp)
                )
                TextButton(
                    onClick = onNotificationClick,
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(
                        text = "Open settings"
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {

            if (setting.reminderAllowed) {
                Text(
                    text = "Reminders allowed",
                    color = Color.Gray
                )
            } else {
                Text(
                    text = "Reminders not allowed",
                    modifier = Modifier.padding(10.dp, 0.dp)
                )
                TextButton(
                    onClick = onReminderClick,
                    colors = ButtonDefaults.buttonColors()
                ) {
                    Text(
                        text = "Open settings"
                    )
                }
            }
        }
    }

    HorizontalDivider()
}

@Preview
@Composable
fun TodoListSettingPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            SettingScreen(
                setting = Setting(true, true, reminderAllowed = false),
                onChange = {},
                onNotificationClick = {},
                onReminderClick = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}