package pl.slaszu.todoapp.ui.element.top

import android.annotation.SuppressLint
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteList

@Composable
fun TopBar(
    navController: NavController,
    onOptionClick: () -> Unit,
    onChangeSearch: (String?) -> Unit,
    searchText: String?
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isListRoute = navBackStackEntry?.destination?.hasRoute(TodoAppRouteList::class) ?: true;

    var searchToggle by remember { mutableStateOf(false) }

    if (isListRoute && searchToggle) {
        SearchTopBar(
            text = searchText,
            onCancelClick = {
                searchToggle = false
                onChangeSearch(null)
            },
            onChange = onChangeSearch
        )
    } else {
        StandardTopBar(
            isListRoute = isListRoute,
            onBackClick = { navController.navigate(TodoAppRouteList) },
            onOptionClick = onOptionClick,
            onSearchClick = { searchToggle = true }
        )
    }


}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview
@Composable
fun TopBarPreview() {
    Scaffold(
        topBar = {
            TopBar(
                navController = rememberNavController(),
                onOptionClick = {},
                onChangeSearch = {},
                searchText = null
            )
        }
    ) {

    }
}