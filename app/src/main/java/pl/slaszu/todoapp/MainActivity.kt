package pl.slaszu.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.ui.element.TodoForm
import pl.slaszu.todoapp.ui.element.TodoList
import pl.slaszu.todoapp.ui.element.TodoListSettings
import pl.slaszu.todoapp.ui.element.TopBar
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteEditOrNewForm
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteList
import pl.slaszu.todoapp.ui.theme.TodoAppTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val todoViewModel: TodoViewModel = viewModel()
            var toggleOptions by remember { mutableStateOf(false) }

            val setting = todoViewModel.settingFlow.collectAsStateWithLifecycle(Setting()).value
            val todoList = todoViewModel.todoListFlow.collectAsStateWithLifecycle(emptyList()).value

            TodoAppTheme {
                Scaffold(
                    topBar = {
                        TopBar(
                            navController = navController,
                            onAddClick = { todoViewModel.loadTodoItemToEditForm(null) },
                            onOptionClick = { toggleOptions = !toggleOptions }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        if (toggleOptions) {
                            TodoListSettings(
                                setting = setting,
                                onChange = { setting -> todoViewModel.saveSetting(setting) }
                            )
                        }
                        NavHost(
                            navController = navController,
                            startDestination = TodoAppRouteList
                        ) {
                            composable<TodoAppRouteList> {
                                TodoList(
                                    items = todoList,
                                    onCheck = { item, checked ->
                                        todoViewModel.check(
                                            item,
                                            checked
                                        )
                                    },
                                    onEdit = { item ->
                                        todoViewModel.loadTodoItemToEditForm(item.id)
                                        navController.navigate(TodoAppRouteEditOrNewForm(todoId = item.id))
                                    },
                                    onDelete = { item -> todoViewModel.delete(item) },
                                )
                            }
                            composable<TodoAppRouteEditOrNewForm> { navStackEntry ->
                                TodoForm(
                                    item = todoViewModel.todoEditModel.value,
                                    onSave = { item ->
                                        todoViewModel.save(item)
                                        navController.navigate(TodoAppRouteList)
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}



