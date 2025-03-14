package pl.slaszu.todoapp.ui.element.top

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import pl.slaszu.todoapp.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTopBar(
    isListRoute: Boolean,
    onOptionClick: () -> Unit,
    onSearchClick: () -> Unit,
    onBackClick: () -> Unit
) {
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