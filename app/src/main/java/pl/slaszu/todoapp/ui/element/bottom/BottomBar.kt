package pl.slaszu.todoapp.ui.element.bottom

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.setting.Setting

@Composable
fun BottomBar(
    setting: Setting,
    onClickNotification: () -> Unit,
    onClickReminder: () -> Unit
) {
    BottomAppBar(
        modifier = Modifier.height(100.dp)
    ) {
        Column {
            if (!setting.notificationAllowed) {
                AssistChip(
                    onClick = onClickNotification,
                    label = { Text("Brak uprawnień dla powiadomień") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Localized description",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                )
            }
            if (!setting.reminderAllowed) {
                AssistChip(
                    onClick = onClickReminder,
                    label = { Text("Brak uprawnień dla powiadomień") },
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Settings,
                            contentDescription = "Localized description",
                            Modifier.size(AssistChipDefaults.IconSize)
                        )
                    }
                )
            }
        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(
        setting = Setting(),
        onClickReminder = {},
        onClickNotification = {}
    )
}