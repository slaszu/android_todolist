package pl.slaszu.todoapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.navigation.TodoAppReminderItems
import pl.slaszu.todoapp.domain.navigation.TodoAppRouteEditOrNewForm
import pl.slaszu.todoapp.domain.navigation.TodoAppRouteList
import pl.slaszu.todoapp.domain.navigation.TodoAppSetting
import pl.slaszu.todoapp.domain.notification.NotificationPermissionLauncher
import pl.slaszu.todoapp.domain.notification.NotificationPermissionService
import pl.slaszu.todoapp.domain.notification.NotificationService
import pl.slaszu.todoapp.domain.reminder.ReminderPermissionLauncher
import pl.slaszu.todoapp.domain.reminder.ReminderPermissionService
import pl.slaszu.todoapp.domain.reminder.exact.ReminderExactManager
import pl.slaszu.todoapp.domain.setting.SettingManager
import pl.slaszu.todoapp.domain.todo.TodoItemType
import pl.slaszu.todoapp.ui.element.bottom.BottomBar
import pl.slaszu.todoapp.ui.element.form.TodoForm
import pl.slaszu.todoapp.ui.element.list.TodoFloatingActionButton
import pl.slaszu.todoapp.ui.element.list.TodoListScreen
import pl.slaszu.todoapp.ui.element.remiander.ReminderDialog
import pl.slaszu.todoapp.ui.element.setting.SettingScreen
import pl.slaszu.todoapp.ui.element.top.TopBar
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import pl.slaszu.todoapp.ui.view_model.FormViewModel
import pl.slaszu.todoapp.ui.view_model.ListViewModel
import pl.slaszu.todoapp.ui.view_model.SettingViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingManager: SettingManager

    @Inject
    lateinit var reminderExactManager: ReminderExactManager

    @Inject
    lateinit var notificationPermissionService: NotificationPermissionService

    @Inject
    lateinit var notificationPermissionLauncher: NotificationPermissionLauncher

    @Inject
    lateinit var reminderPermissionService: ReminderPermissionService

    @Inject
    lateinit var reminderPermissionLauncher: ReminderPermissionLauncher


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val reminderIds = this.getReminderItemIds()

        val permissionNotification = this.notificationPermissionService.hasPermission()
        val permissionReminder = this.reminderPermissionService.hasPermission()

        var launcherStartActivityNotification: ActivityResultLauncher<Intent>? = null
        if (!permissionNotification) {
            launcherStartActivityNotification =
                this.notificationPermissionLauncher.registerStartActivityLauncher(this) {
                    Log.d("myapp", "launcherStartActivityPermission = $it")
                    recreate()
                }
            val launcherRequestPermission =
                this.notificationPermissionLauncher.registerRequestPermissionLauncher(this) {
                    Log.d("myapp", "launcherRequestPermission = $it")
                    recreate()
                }
            if (launcherRequestPermission != null) {
                lifecycleScope.launch {
                    delay(5000)
                    notificationPermissionLauncher.launch(launcherRequestPermission)
                }
            }
        }

        var launchStartActivityReminder: ActivityResultLauncher<Intent>? = null
        if (!permissionReminder) {
            launchStartActivityReminder =
                this.reminderPermissionLauncher.registerStartActivityLauncher(this) {
                    Log.d("myapp", "launchStartActivityReminder = $it")
                    recreate()
                }
        }

        lifecycleScope.launch {
            settingManager.bootstrap()
            if (permissionReminder) {
                reminderExactManager.bootstrap()
            }
        }

        setContent {
            val navController = rememberNavController()
            val listViewModel: ListViewModel = viewModel()
            val formViewModel: FormViewModel = viewModel()
            val settingViewModel: SettingViewModel = viewModel()

            var searchString by rememberSaveable { mutableStateOf<String?>(null) }

            val setting =
                settingViewModel.settingFlow.collectAsState(null).value
            val todoList =
                listViewModel.getTodoList(searchString)
                    .collectAsStateWithLifecycle(null).value


            val snackbarHostState = remember { SnackbarHostState() }

            var tabSelectedRemember by rememberSaveable { mutableStateOf(TodoItemType.TIMELINE) }

            if (setting == null || todoList == null) {
                TodoAppTheme {
                    Scaffold { innerPadding ->
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(innerPadding)
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.width(100.dp),
                                color = MaterialTheme.colorScheme.error,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
                return@setContent
            }

            TodoAppTheme {
                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackbarHostState)
                    },
                    topBar = {
                        TopBar(
                            searchText = searchString,
                            onChangeSearch = {
                                searchString = it
                            },
                            navController = navController,
                            onOptionClick = { navController.navigate(TodoAppSetting) }
                        )
                    },
                    bottomBar = {
                        if (permissionNotification && permissionReminder) {
                            return@Scaffold
                        }
                        BottomBar(
                            permissionNotification = permissionNotification,
                            permissionReminder = permissionReminder,
                            onClickNotification = {
                                if (launcherStartActivityNotification != null) {
                                    notificationPermissionLauncher.launch(
                                        launcherStartActivityNotification
                                    )
                                }
                            },
                            onClickReminder = {
                                if (launchStartActivityReminder != null) {
                                    reminderPermissionLauncher.launch(
                                        launchStartActivityReminder
                                    )
                                }
                            }
                        )
                    },
                    floatingActionButton = {
                        TodoFloatingActionButton(
                            navController = navController,
                            onClick = {
                                formViewModel.loadTodoItemToEditForm(0)
                                navController.navigate(TodoAppRouteEditOrNewForm)
                            }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        NavHost(
                            navController = navController,
                            startDestination = if (reminderIds.isEmpty()) {
                                TodoAppRouteList
                            } else {
                                TodoAppReminderItems(reminderIds)
                            }
                        ) {
                            composable<TodoAppRouteList> {
                                Column {
                                    TodoListScreen(
                                        generalItemList = todoList.filter {
                                            it.startDate == null && !it.done
                                        },
                                        timelineItemList = todoList.filter {
                                            it.startDate != null && !it.done
                                        }.let {
                                            PresentationService.convertToTimelineMap(it)
                                        },
                                        doneTimelineList = todoList.filter {
                                            it.done
                                        }.let {
                                            PresentationService.convertToTimelineMap(it)
                                        },
                                        setting = setting,
                                        onCheck = { item, checked ->
                                            listViewModel.check(
                                                item,
                                                checked,
                                                snackbarHostState
                                            )
                                        },
                                        onEdit = { item ->
                                            formViewModel.loadTodoItemToEditForm(item.id)
                                            navController.navigate(TodoAppRouteEditOrNewForm)
                                        },
                                        onDelete = { item ->
                                            listViewModel.delete(
                                                item,
                                                snackbarHostState
                                            )
                                        },
                                        tabSelectedRemember = tabSelectedRemember,
                                        onTabChange = { selectedTabIndex ->
                                            tabSelectedRemember = selectedTabIndex
                                        }
                                    )
                                }
                            }
                            composable<TodoAppRouteEditOrNewForm> {
                                TodoForm(
                                    item = formViewModel.todoEditModel.value,
                                    onSave = { item ->
                                        if (item.startDate == null) {
                                            tabSelectedRemember = TodoItemType.GENERAL
                                        } else {
                                            tabSelectedRemember = TodoItemType.TIMELINE
                                        }
                                        navController.navigate(TodoAppRouteList)
                                        formViewModel.save(item, snackbarHostState)
                                    }
                                )
                            }
                            composable<TodoAppSetting> {
                                SettingScreen(
                                    setting = setting,
                                    onChange = {
                                        settingViewModel.saveSetting(it, setting)
                                    }
                                )
                            }
                            composable<TodoAppReminderItems> { backStackEntry ->
                                val param = backStackEntry.toRoute<TodoAppReminderItems>()
                                ReminderDialog(
                                    items = todoList.filter {
                                        param.ids.contains(it.id)
                                    },
                                    onDismiss = { navController.navigate(TodoAppRouteList) },
                                    onCloseItem = { item ->
                                        listViewModel.check(item, true, snackbarHostState)
                                    }
                                )
                            }
                        }
                    }

                }
            }
        }
    }

    private fun getReminderItemIds(): LongArray {
        val itemIds = this.intent.getLongArrayExtra(NotificationService.INTENT_KEY) ?: longArrayOf()
        Log.d(
            "myapp",
            "Reminder item ids (from intent)[${itemIds.size}]: ${itemIds.joinToString()}"
        )
        return itemIds;
    }

}



