package pl.slaszu.todoapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.SettingRepository
import pl.slaszu.todoapp.domain.TodoItemType
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import pl.slaszu.todoapp.domain.notification.NotificationPermissionService
import pl.slaszu.todoapp.domain.notification.NotificationService
import pl.slaszu.todoapp.domain.reminder.ReminderExactService
import pl.slaszu.todoapp.domain.reminder.ReminderPermission
import pl.slaszu.todoapp.domain.reminder.ReminderRepeatService
import pl.slaszu.todoapp.ui.element.form.TodoForm
import pl.slaszu.todoapp.ui.element.list.TodoFloatingActionButton
import pl.slaszu.todoapp.ui.element.list.TodoListScreen
import pl.slaszu.todoapp.ui.element.remiander.ReminderDialog
import pl.slaszu.todoapp.ui.element.setting.SettingScreen
import pl.slaszu.todoapp.ui.element.top.TopBar
import pl.slaszu.todoapp.ui.navigation.TodoAppReminderItems
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteEditOrNewForm
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteList
import pl.slaszu.todoapp.ui.navigation.TodoAppSetting
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import pl.slaszu.todoapp.ui.view_model.FormViewModel
import pl.slaszu.todoapp.ui.view_model.ListViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingRepository: SettingRepository

    private val notificationPermissionService = NotificationPermissionService(this)

    @Inject
    lateinit var reminderPermissionService: ReminderPermission

    @Inject
    lateinit var repository: TodoRepository<TodoModel>

    private val reminderExactService = ReminderExactService(this)

    private val reminderRepeatService = ReminderRepeatService(this)


//    // TODO: change it to "ActivityResultContract" solution
//    @Deprecated("This method has been deprecated")
//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when (requestCode) {
//            NotificationPermissionService.NOTIFICATION_REQUEST_CODE
//            -> {
//                this.updateNotificationAllowed(
//                    allowed = this.notificationPermissionService.isPermissionGranted(
//                        permissions,
//                        grantResults
//                    )
//                )
//            }
//
//            else -> return
//        }
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val reminderIds = this.getReminderItemIds()

        setContent {
            val navController = rememberNavController()
            val listViewModel: ListViewModel = viewModel()
            val formViewModel: FormViewModel = viewModel()

            var searchString by remember { mutableStateOf<String?>(null) }

            val setting =
                listViewModel.settingFlow.collectAsStateWithLifecycle(Setting()).value
            val todoList =
                listViewModel.getTodoList(searchString).collectAsStateWithLifecycle(emptyList()).value


            val snackbarHostState = remember { SnackbarHostState() }

            var tabSelectedRemember by remember { mutableStateOf(TodoItemType.TIMELINE) }


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
                                        formViewModel.save(item, snackbarHostState) { savedItem ->
                                            reminderExactService.schedule(savedItem)
                                        }
                                    }
                                )
                            }
                            composable<TodoAppSetting> {
                                SettingScreen(
                                    setting = setting,
                                    onChange = {
                                        listViewModel.saveSetting(it, setting)
                                    },
                                    onReminderClick = { reminderPermissionService.openSettingActivity() },
                                    onNotificationClick = { notificationPermissionService.openSettingActivity() }
                                )
                            }
                            composable<TodoAppReminderItems> { backStackEntry ->
                                val param = backStackEntry.toRoute<TodoAppReminderItems>()
                                ReminderDialog(
                                    reminderItemsId = param.ids,
                                    items = todoList,
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

        this.checkSystemSettings()
        this.getReminderItemIds()
//        val notificationService = NotificationService(this)
//        lifecycleScope.launch {
//            repository.getTodoList().collect { items ->
//                val items = items.subList(0,4)
//                notificationService.sendNotification(items!!.toTypedArray())
//            }
//
//        }
    }

    private fun getReminderItemIds(): LongArray {
        val itemIds = this.intent.getLongArrayExtra(NotificationService.INTENT_KEY) ?: longArrayOf()
        Log.d(
            "myapp",
            "Reminder item ids (from intent)[${itemIds.size}]: ${itemIds.joinToString()}"
        )
        return itemIds;
    }

    override fun onRestart() {
        super.onRestart()
        this.checkSystemSettings()
        this.getReminderItemIds()
    }

    private fun checkSystemSettings() {
        this.updateSystemSettings(
            notificationAllowed = this.notificationPermissionService.hasPermission(),
            reminderAllowed = this.reminderPermissionService.hasPermission()
        )
    }

    private fun updateSystemSettings(notificationAllowed: Boolean, reminderAllowed: Boolean) {
        lifecycleScope.launch {
            settingRepository.getData().cancellable().collect { setting ->
                val refreshSetting = setting.copy(
                    notificationAllowed = notificationAllowed,
                    reminderAllowed = reminderAllowed
                )

                reminderRepeatService.scheduleRepeatOnePerDay(
                    hour = refreshSetting.reminderRepeatHour,
                    minute = refreshSetting.reminderRepeatMinute
                )

                settingRepository.saveData(refreshSetting)
                this.cancel()
            }
        }

    }


}



