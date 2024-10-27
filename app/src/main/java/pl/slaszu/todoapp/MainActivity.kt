package pl.slaszu.todoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
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

            TodoAppTheme {
                Scaffold(
                    topBar = {
                        TopBar(
                            todoViewModel = todoViewModel,
                            navController = navController
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = TodoAppRouteList,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable<TodoAppRouteList> {
                            TodoListScreen(
                                todoViewModel = todoViewModel,
                                navController = navController
                            )
                        }
                        composable<TodoAppRouteEditOrNewForm> { navStackEntry ->
                            Log.d("myapp","recreate composable")

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



