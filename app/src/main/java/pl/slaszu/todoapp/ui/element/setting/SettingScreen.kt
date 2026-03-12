package pl.slaszu.todoapp.ui.element.setting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Login
import androidx.compose.material.icons.rounded.NotificationsActive
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.BuildConfig
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.ui.element.form.TimeDialogModel

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
    val scrollState = rememberScrollState()
    var chooseTimeDialog by remember { mutableStateOf(false) }

    if (chooseTimeDialog) {
        TimeDialogModel(
            timePickerState = rememberTimePickerState(
                initialHour = setting.reminderRepeatHour,
                initialMinute = setting.reminderRepeatMinute,
                is24Hour = true
            ),
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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        // --- SEKCA PROFILU (Firebase Auth) ---
        UserAccountCard(
            user = user,
            onLogIn = onLogIn,
            onLogOut = onLogOut
        )

        Text(
            text = "Preferencje aplikacji",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.Bold
        )

        // --- KARTA USTAWIEŃ ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                    alpha = 0.4f
                )
            )
        ) {
            Column {
                SettingRow(
                    icon = Icons.Rounded.NotificationsActive,
                    title = stringResource(R.string.setting_daily),
                    subtitle = stringResource(R.string.setting_daily_info),
                    trailingText = "${setting.reminderRepeatHour}:${
                        setting.reminderRepeatMinute.toString().padStart(2, '0')
                    }",
                    onClick = { chooseTimeDialog = true }
                )

                HorizontalDivider(Modifier.padding(horizontal = 16.dp), thickness = 0.5.dp)

                SettingRow(
                    icon = Icons.Rounded.Visibility,
                    title = stringResource(R.string.showhidden_title),
                    subtitle = stringResource(R.string.showhidden_desc),
                    trailingContent = {
                        Switch(
                            checked = setting.showFinished,
                            onCheckedChange = { onChange(setting.copy(showFinished = it)) }
                        )
                    }
                )
            }
        }

        // --- STOPKA Z WERSJĄ ---
        Spacer(modifier = Modifier.weight(1f))

        Surface(
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
            shape = CircleShape
        ) {
            Text(
                text = "v${BuildConfig.VERSION_NAME}",
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun UserAccountCard(
    user: User?,
    onLogIn: () -> Unit,
    onLogOut: () -> Unit
) {
    val gradient = Brush.verticalGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer
        )
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                // Naprawione: teraz obie opcje zwracają Brush
                .background(if (user == null) SolidColor(Color.Transparent) else gradient)
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (user == null) {
                Text(
                    "Twoje dane nie są bezpieczne",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "Zaloguj się, aby synchronizować zadania w chmurze.",
                    style = MaterialTheme.typography.bodySmall,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(16.dp))
                Button(
                    onClick = onLogIn,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Rounded.Login, null)
                    Spacer(Modifier.width(8.dp))
                    Text("Zaloguj przez Firebase")
                }
            } else {
                Text(
                    user.email ?: "Użytkownik zalogowany",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    "Synchronizacja aktywna",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White.copy(alpha = 0.8f)
                )
                Spacer(Modifier.height(16.dp))
                OutlinedButton(
                    onClick = onLogOut,
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = SolidColor(Color.White)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Wyloguj się")
                }
            }
        }
    }
}

@Composable
fun SettingRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailingText: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
    onClick: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(40.dp),
            shape = RoundedCornerShape(10.dp),
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        ) {
            Icon(
                icon,
                null,
                modifier = Modifier.padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(modifier = Modifier
            .padding(start = 16.dp)
            .weight(1f)) {
            Text(
                title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        if (trailingText != null) {
            Text(
                trailingText,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }

        trailingContent?.invoke()
    }
}