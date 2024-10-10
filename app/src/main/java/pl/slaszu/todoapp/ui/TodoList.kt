package pl.slaszu.todoapp.ui

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TodoList(
    items: List<String>,
    modifier: Modifier = Modifier
    ) {
    LazyColumn {
        items(items) { text ->
            TodoListItem(
                text = text,
                onCheck = {}
            )
        }
    }
}


@Preview
@Composable
fun TodoListPreview() {
    TodoList(
        items = List(5) { i -> "Task number $i" }
    )
}