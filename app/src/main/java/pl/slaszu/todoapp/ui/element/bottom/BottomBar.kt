package pl.slaszu.todoapp.ui.element.bottom

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.R

@Composable
fun BottomBar(
    permissionNotification: Boolean,
    permissionReminder: Boolean,
    onClickNotification: () -> Unit,
    onClickReminder: () -> Unit
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

    BottomAppBar {

        if (!permissionNotification) {

            TextButton(
                modifier = Modifier.weight(0.5f),
                onClick = onClickNotification,
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {

                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(stringResource(R.string.permission_notification))
                }
            }

        }
        if (!permissionReminder) {
            TextButton(
                modifier = Modifier.weight(0.5f),
                onClick = onClickReminder,
                colors = ButtonDefaults.textButtonColors().copy(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                AnimatedVisibility(
                    visible = visible,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Text(stringResource(R.string.permission_reminder))
                }
            }

        }
    }
}

@Preview
@Composable
fun BottomBarPreview() {
    BottomBar(
        permissionNotification = false,
        permissionReminder = false,
        onClickReminder = {},
        onClickNotification = {}
    )
}