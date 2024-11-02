package pl.slaszu.todoapp.ui.element

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteEditOrNewForm
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
    onAddClick: () -> Unit,
    onOptionClick: () -> Unit

) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isListRoute = navBackStackEntry?.destination?.hasRoute(TodoAppRouteList::class) ?: true;

    TopAppBar(
        title = {
            Text(stringResource(R.string.top_bar_title))
        },
        actions = {
            if (!isListRoute) return@TopAppBar

            IconButton(
                onClick = {
                    onAddClick()
                    navController.navigate(TodoAppRouteEditOrNewForm())
                }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add new"
                )
            }

            IconButton(
                onClick = { onOptionClick() }
            ) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "More options"
                )
            }

        },
        navigationIcon = {
            if (isListRoute) return@TopAppBar

            IconButton(
                onClick = {
                    navController.navigate(TodoAppRouteList)
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to list"
                )
            }
        }
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun TopBarPreview() {
    Scaffold(
        topBar = {
            TopBar(
                navController = rememberNavController(),
                onAddClick = {},
                onOptionClick = {}
            )
        }
    ) {

    }
}