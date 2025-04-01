package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.todo.FakeTodoModel
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun TodoDeleteConfirmAlertDialog(
    item: TodoModel,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        icon = {
            Row {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete Icon")
                Text(
                    text = stringResource(R.string.todo_delete_confirm_title),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        text = {
            Column {
                Text(
                    text = item.text,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(0.dp, 5.dp)
                )

                Text(
                    text = stringResource(R.string.todo_delete_confirm_text),
                    fontWeight = FontWeight.Bold
                )
            }
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
                item = FakeTodoModel(
                    text = "Jakieś tam przypomnienie z testowym wpisem"
                ),
                modifier = Modifier.padding(it)
            )
        }
    }
}