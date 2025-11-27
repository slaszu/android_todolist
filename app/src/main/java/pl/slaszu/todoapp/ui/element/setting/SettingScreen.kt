package pl.slaszu.todoapp.ui.element.setting

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.BuildConfig
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.ui.element.form.TimeDialogModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    setting: Setting,
    onChange: (Setting) -> Unit,
    user: User?,
    onLogIn: () -> Unit,
    onLogOut: () -> Unit,
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
            text = stringResource(R.string.synchro_title),
            description = user?.email ?: stringResource(R.string.synchro_desc),
            important = user == null
        ) {
            if (user === null) {
                Button(
                    onClick = onLogIn
                ) {
                    Text(stringResource(R.string.login))
                }
            } else {
                Button(
                    onClick = onLogOut
                ) {
                    Text(stringResource(R.string.logout))
                }
            }
        }

        SettingOption(
            text = stringResource(R.string.setting_daily),
            description = stringResource(R.string.setting_daily_info)
        ) {
            Button(
                onClick = { chooseTimeDialog = true },
                modifier = Modifier.testTag("repeat_time")
            ) {
                Text(
                    text = "${setting.reminderRepeatHour}:${
                        setting.reminderRepeatMinute.toString().padStart(2, '0')
                    }"
                )
            }
        }

        SettingOption(
            text = stringResource(R.string.showhidden_title),
            description = stringResource(R.string.showhidden_desc)
        ) {
            Button(
                onClick = { onChange(setting.copy(showFinished = !setting.showFinished)) },
                modifier = Modifier.testTag("finished")
            ) {
                Text(
                    text = if (setting.showFinished) stringResource(R.string.yes) else stringResource(R.string.no)
                )
            }
        }


        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter // Aligns children to the bottom center
        ) {
            Text(
                text = "application version: ${BuildConfig.VERSION_NAME}",
                modifier = Modifier.padding(16.dp) // Add some padding
            )
        }
    }
}

@Composable
private fun SettingOption(
    text: String,
    description: String,
    important: Boolean = false,
    content: @Composable () -> Unit
) {

    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                delay(500)
                visible = !visible
            }
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp, 10.dp)
    ) {

        Column(
            modifier = Modifier.weight(0.9f)
        ) {
            BadgedBox(
                badge = {
                    if (important) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {
                            Badge(
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            ) {
                Text(
                    text = text,
                    fontSize = TextUnit(4f, TextUnitType.Em),
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
                setting = Setting(),
                onChange = {},
                modifier = Modifier.padding(it),
                user = null,//User("asdasdasdasd","testowy.email@com.pl"),
                onLogOut = {},
                onLogIn = {}
            )
        }
    }
}