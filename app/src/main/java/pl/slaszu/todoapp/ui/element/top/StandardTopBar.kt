package pl.slaszu.todoapp.ui.element.top

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.navigation.TodoAppRouteEditOrNewForm
import pl.slaszu.todoapp.domain.navigation.TodoAppRouteList
import pl.slaszu.todoapp.domain.navigation.TodoAppSetting

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardTopBar(
    isListRoute: Boolean,
    route: String? = null,
    onOptionClick: () -> Unit,
    onSearchClick: () -> Unit,
    onBackClick: () -> Unit,
    user: User?
) {
    var badgeVisible by remember { mutableStateOf(true) }

    LaunchedEffect(user) {
        if (user == null) {
            while (true) {
                delay(800)
                badgeVisible = !badgeVisible
            }
        }
    }

    // Usunięto Surface i paddingi zewnętrzne, aby bar był "płaski"
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = Color.Transparent, // Bar będzie miał tło takie jak Scaffold
        ),
        title = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = stringResource(R.string.top_bar_title),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = when (route) {
                        TodoAppRouteList::class.qualifiedName -> stringResource(R.string.top_bar_my_tasks)
                        TodoAppRouteEditOrNewForm::class.qualifiedName -> stringResource(R.string.top_bar_edit_task)
                        TodoAppSetting::class.qualifiedName -> stringResource(R.string.top_bar_settings)
                        else -> ""
                    },
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                )
            }
        },
        navigationIcon = {
            if (!isListRoute) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.top_bar_back),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        actions = {
            if (isListRoute) {
                IconButton(onClick = onSearchClick) {
                    Icon(
                        imageVector = Icons.Rounded.Search,
                        contentDescription = stringResource(R.string.top_bar_search),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                BadgedBox(
                    modifier = Modifier.padding(end = 4.dp),
                    badge = {
                        if (user == null) {
                            Row {
                                AnimatedVisibility(
                                    visible = badgeVisible,
                                    enter = fadeIn(animationSpec = tween(400)) + scaleIn(),
                                    exit = fadeOut(animationSpec = tween(400)) + scaleOut()
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .clip(CircleShape)
                                            .background(MaterialTheme.colorScheme.error)
                                    )
                                }
                            }
                        }
                    }
                ) {
                    IconButton(onClick = onOptionClick) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = stringResource(R.string.top_bar_more_options),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    )
}
