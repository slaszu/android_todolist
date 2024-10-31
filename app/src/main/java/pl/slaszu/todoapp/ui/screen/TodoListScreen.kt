package pl.slaszu.todoapp.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import pl.slaszu.todoapp.TodoViewModel
import pl.slaszu.todoapp.ui.element.TodoList
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteEditOrNewForm

@Composable
fun TodoListScreen(
    todoViewModel: TodoViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val todoList = todoViewModel.todoListFlow.collectAsStateWithLifecycle(
        initialValue = emptyList()
    ).value

    Column {
        TodoList(
            items = todoList,
            onCheck = { item, checked -> todoViewModel.check(item, checked) },
            onEdit = { item ->
                todoViewModel.loadTodoItemToEditForm(item.id)
                navController.navigate(TodoAppRouteEditOrNewForm(todoId = item.id))
            },
            onDelete = { item -> todoViewModel.delete(item) },
            modifier = modifier
        )
    }
}