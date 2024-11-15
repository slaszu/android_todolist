package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoModelFake
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@Composable
fun TodoList(
    items: List<TodoModel>,
    onCheck: (TodoModel, Boolean) -> Unit,
    onEdit: (TodoModel) -> Unit,
    onDelete: (TodoModel) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(
            items = items
        ) { todoItem ->
                TodoListItem(
                    item = todoItem,
                    onCheckItem = { checked -> onCheck(todoItem, checked) },
                    onEditItem = { onEdit(todoItem) },
                    onDeleteItem = { onDelete(todoItem) },
                    modifier = modifier
                )

        }
    }
}

@Composable
fun TodoListItem(
    item: TodoModel,
    onCheckItem: (Boolean) -> Unit,
    onEditItem: () -> Unit,
    onDeleteItem: () -> Unit,
    modifier: Modifier = Modifier
) {

    var alertDialog by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {

        Checkbox(
            checked = item.done,
            onCheckedChange = { checked ->
                onCheckItem(checked)
            },
            modifier = modifier.weight(0.1f)
        )

        Text(
            text = item.text,
            textDecoration = TextDecoration.LineThrough.takeIf { item.done },
            modifier = modifier
                .weight(0.9f)
                .padding(start = 16.dp)
        )

        IconButton(
            onClick = { onEditItem() }
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription = "Edit item"
            )
        }

        IconButton(
            onClick = { alertDialog = true }
        ) {
            Icon(
                Icons.Filled.Delete,
                contentDescription = "Delete item"
            )
        }
    }

    if (alertDialog) {
        TodoDeleteConfirmAlertDialog(
            onConfirm = {
                onDeleteItem()
                alertDialog = false
            },
            onDismiss = {
                alertDialog = false
            }
        )
    }

    HorizontalDivider()
}

@Preview
@Composable
fun TodoListPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            TodoList(
                items = List(5) { i ->
                    TodoModelFake(text = "Todo item nr $i")
                },
                onCheck = { _, _ -> },
                onEdit = {},
                onDelete = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}