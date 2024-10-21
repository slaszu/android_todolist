package pl.slaszu.todoapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import pl.slaszu.todoapp.TodoViewModel
import pl.slaszu.todoapp.ui.element.TodoList

@Composable
fun TodoListScreen(
    todoViewModel: TodoViewModel,
    modifier: Modifier = Modifier
) {
    Column {
        Text(text = "List")
        TodoList(
            items = todoViewModel.todoListFlow.collectAsStateWithLifecycle(
                initialValue = emptyList()
            ).value,
            onCheck = { item, checked -> todoViewModel.toggleDone(item, checked) },
            //modifier = modifier
        )
    }
}