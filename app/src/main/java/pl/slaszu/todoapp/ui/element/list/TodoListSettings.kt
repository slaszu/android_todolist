package pl.slaszu.todoapp.ui.element.list

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun TodoListSettings(
    setting: Setting,
    onChange: (Setting) -> Unit,
    onNotificationClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text(text = stringResource(R.string.setting_show_done))
            Switch(
                checked = setting.showDone,
                onCheckedChange = { change ->
                    onChange(setting.copy(showDone = change))
                },
                modifier = Modifier.padding(10.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier.fillMaxWidth()
        ) {

            if (setting.notificationAllowed) {
                Text(text = "Notifications allowed")
            } else {
                Text(text = "Notification not allowed")
                TextButton(
                    onClick = onNotificationClick,
                    colors = ButtonDefaults.buttonColors(),
                    modifier = Modifier.padding(10.dp)
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
            TodoListSettings(
                setting = Setting(),
                onChange = {},
                onNotificationClick = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}