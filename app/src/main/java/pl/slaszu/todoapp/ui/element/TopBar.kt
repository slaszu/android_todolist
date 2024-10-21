package pl.slaszu.todoapp.ui.element

import android.media.Image
import android.provider.MediaStore.Images
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.ui.screen.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
) {
    TopAppBar(
//        colors = TopAppBarDefaults.topAppBarColors(
//            containerColor = MaterialTheme.colorScheme.primaryContainer,
//            titleContentColor = MaterialTheme.colorScheme.primary,
//        ),
        title = {
            Text(stringResource(R.string.top_bar_title))
        },
        actions = {
            IconButton(
                onClick = {
                    navController.navigate(Routes.TODO_FORM.name)
                }
            ) {
                Icon(
                    Icons.Filled.Add,
                    contentDescription = "Add new"
                )
            }
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navController.navigate(Routes.TODO_LIST.name)
                },
                enabled = navController.currentDestination?.route != Routes.TODO_LIST.name
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back to list"
                )
            }
        }
    )
}

@Preview
@Composable
fun TopBarPreview() {
    Scaffold(
        topBar = {
            TopBar(
                navController = rememberNavController()
            )
        }
    ) {

    }
}