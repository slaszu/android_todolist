package pl.slaszu.todoapp

import android.os.Bundle
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
import dagger.hilt.android.AndroidEntryPoint
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.ui.element.BottomBar
import pl.slaszu.todoapp.ui.element.TodoForm
import pl.slaszu.todoapp.ui.element.TopBar
import pl.slaszu.todoapp.ui.screen.Routes
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
                            navController = navController
                        )
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Routes.TODO_LIST.name,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable(route = Routes.TODO_LIST.name) {
                            TodoListScreen(
                                todoViewModel = todoViewModel,
                            )
                        }
                        composable(route = Routes.TODO_FORM.name) {
                            TodoForm(
                                item = TodoModel(text = ""),
                                onSave = { item ->
                                    todoViewModel.save(item)
                                    navController.navigate(Routes.TODO_LIST.name)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}



