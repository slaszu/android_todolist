package pl.slaszu.todoapp.ui.element.top

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.auth.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTopBar(
    isListRoute: Boolean,
    onOptionClick: () -> Unit,
    onSearchClick: () -> Unit,
    onBackClick: () -> Unit,
    user: User?
) {

    val scope = rememberCoroutineScope()
    var visible by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        scope.launch {
            while (true) {
                delay(500)
                visible = !visible
            }
        }
    }

    TopAppBar(
        modifier = Modifier.padding(5.dp),
        title = {
            Text(stringResource(R.string.top_bar_title))
        },
        actions = {
            if (!isListRoute) return@TopAppBar

            IconButton(
                onClick = {
                    onSearchClick()
                }
            ) {
                Icon(
                    Icons.Filled.Search,
                    contentDescription = "Search icon"
                )
            }

            BadgedBox(
                badge = {
                    if (user == null) {
                        androidx.compose.animation.AnimatedVisibility(
                            visible = visible,
                            enter = fadeIn(),
                            exit = fadeOut()
                        ) {

                            Badge(
                                modifier = Modifier.size(10.dp)
                            )
                        }
                    }
                }
            ) {
                IconButton(
                    onClick = { onOptionClick() }
                ) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "More options"
                    )
                }
            }

        },
        navigationIcon = {
            if (isListRoute) return@TopAppBar

            IconButton(
                onClick = {
                    onBackClick()
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