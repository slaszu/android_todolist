package pl.slaszu.todoapp.ui.element.list

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import pl.slaszu.todoapp.domain.navigation.TodoAppRouteList

@Composable
fun TodoFloatingActionButton(
    navController: NavController,
    onClick: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isListRoute = navBackStackEntry?.destination?.hasRoute(TodoAppRouteList::class) ?: true

    if (!isListRoute) return

    FloatingActionButton(
        onClick = { onClick() },
        // Zmieniamy kształt na bardziej nowoczesny (zaokrąglony kwadrat/squircle)
        // Pasuje to do kart zadań, które mają 16.dp
        shape = RoundedCornerShape(18.dp),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 6.dp,
            pressedElevation = 2.dp
        )
    ) {
        Icon(
            imageVector = Icons.Filled.Add,
            contentDescription = "Add new todo",
            modifier = Modifier.size(28.dp) // Nieco większa ikona dla lepszego balansu
        )
    }
}