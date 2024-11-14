package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun TodoDeleteConfirmAlertDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        icon = {
            Icon(Icons.Outlined.Delete, contentDescription = "Delete Icon")
        },
        title = {
            Text(text = stringResource(R.string.todo_delete_confirm_title))
        },
        text = {
            Text(text = stringResource(R.string.todo_delete_confirm_text))
        },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onConfirm
            ) {
                Text(stringResource(R.string.btn_delete))
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss
            ) {
                Text(stringResource(R.string.btn_back))
            }
        }
    )
}

@Preview
@Composable
fun TodoDeleteConfirmAlertDialogPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            TodoDeleteConfirmAlertDialog(
                onConfirm = {},
                onDismiss = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}