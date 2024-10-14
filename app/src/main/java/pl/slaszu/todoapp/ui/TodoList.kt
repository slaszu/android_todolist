package pl.slaszu.todoapp.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import pl.slaszu.todoapp.domain.TodoModel

@Composable
fun TodoList(
    items: List<TodoModel>,
    onCheck: (TodoModel, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn {
        items(items) { item ->
            TodoListItem(
                item = item,
                onCheckedChange = { checked -> onCheck(item, checked) },
                modifier = modifier
            )
        }
    }
}

//
//@Preview
//@Composable
//fun TodoListPreview() {
//    TodoList(
//        items = List(5) { i -> "Task number $i" }
//    )
//}