package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.todo.FakeTodoModel
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import java.time.LocalDateTime

@Composable
fun TodoList(
    items: List<TodoModel>,
    setting: Setting,
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
                setting = setting,
                onCheckItem = { checked -> onCheck(todoItem, checked) },
                onEditItem = { onEdit(todoItem) },
                onDeleteItem = { onDelete(todoItem) },
                modifier = modifier
            )

        }
    }
}

@Preview
@Composable
fun TodoListPreview() {
    TodoAppTheme {
        Scaffold() { it ->
            TodoList(
                items = List(5) { i ->
                    FakeTodoModel(text = "Todo item nr $i", startDate = LocalDateTime.now())
                },
                setting = Setting(),
                onCheck = { _, _ -> },
                onEdit = {},
                onDelete = {},
                modifier = Modifier.padding(it)
            )
        }
    }
}