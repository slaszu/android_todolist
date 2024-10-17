package pl.slaszu.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults.outlinedButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import pl.slaszu.todoapp.ui.screen.FavoriteListScreen
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
                        Row {
                            Button(
                                colors = outlinedButtonColors(),
                                onClick = { navController.navigate(Routes.TODO_LIST.name) }
                            ) {
                                Text("List")
                            }
                            Button(
                                onClick = { navController.navigate(Routes.FAVORITE_LIST.name) }
                            ) {
                                Text("Favorite")
                            }
                        }
                    },
                    modifier = Modifier.fillMaxSize()
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
                        composable(route = Routes.FAVORITE_LIST.name) {
                            FavoriteListScreen(
                                todoViewModel = todoViewModel,
                            )
                        }
                    }
                }
            }
        }
    }
}



