package pl.slaszu.todoapp.ui.element.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.res.stringResource
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.repeat.RepeatType

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
                            selected = (it.period == type?.period),
                            onClick = {
                                type = it
                            }
                        )
                        Text(
                            text = stringResource(it.translationKey ?: 0)
                        )
                    }

                    HorizontalDivider()
                }
            }
        }
    )
}