package pl.slaszu.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.slaszu.todoapp.ui.element.TodoForm
import pl.slaszu.todoapp.ui.element.TopBar
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteEditOrNewForm
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteList
import pl.slaszu.todoapp.ui.screen.TodoListScreen
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
                            Row(
                                modifier = Modifier.height(200.dp)
                            ) {
                                Text("text")
                            }
                        }
                        NavHost(
                            navController = navController,
                            startDestination = TodoAppRouteList
                        ) {
                            composable<TodoAppRouteList> {
                                TodoListScreen(
                                    todoViewModel = todoViewModel,
                                    navController = navController
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



