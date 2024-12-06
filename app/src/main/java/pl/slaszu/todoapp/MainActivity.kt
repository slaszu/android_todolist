package pl.slaszu.todoapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.cancellable
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.SettingRepository
import pl.slaszu.todoapp.domain.notification.NotificationPermissionService
import pl.slaszu.todoapp.domain.reminder.ReminderPermission
import pl.slaszu.todoapp.infrastructure.reminder.ReminderPermissionService
import pl.slaszu.todoapp.domain.reminder.ReminderService
import pl.slaszu.todoapp.ui.element.form.TodoForm
import pl.slaszu.todoapp.ui.element.list.TodoList
import pl.slaszu.todoapp.ui.element.list.TodoListScreen
import pl.slaszu.todoapp.ui.element.list.TodoListSettings
import pl.slaszu.todoapp.ui.element.list.TopBar
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteEditOrNewForm
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteList
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import pl.slaszu.todoapp.ui.view_model.TodoFormViewModel
import pl.slaszu.todoapp.ui.view_model.TodoListViewModel
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var settingRepository: SettingRepository

    private val notificationPermissionService = NotificationPermissionService(this)

    @Inject
    lateinit var reminderPermissionService: ReminderPermission

    private val reminderService = ReminderService(this)


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



        setContent {
            val navController = rememberNavController()
            val todoListViewModel: TodoListViewModel = viewModel()
            val todoFormViewModel: TodoFormViewModel = viewModel()

            var toggleOptions by remember { mutableStateOf(false) }

            val setting = todoListViewModel.settingFlow.collectAsStateWithLifecycle(Setting()).value
            val todoList =
                todoListViewModel.todoListFlow.collectAsStateWithLifecycle(emptyList()).value

            TodoAppTheme {
                Scaffold(
                    topBar = {
                        TopBar(
                            navController = navController,
                            onAddClick = { todoFormViewModel.loadTodoItemToEditForm(0) },
                            onOptionClick = { toggleOptions = !toggleOptions }
                        )
                    }
                ) { innerPadding ->
                    Column(
                        verticalArrangement = Arrangement.Top,
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        if (toggleOptions) {
                            TodoListSettings(
                                setting = setting,
                                onChange = { setting -> todoListViewModel.saveSetting(setting) },
                                onNotificationClick = { notificationPermissionService.openSettingActivity() },
                                onReminderClick = { reminderPermissionService.openSettingActivity() }
                            )
                        }
                        NavHost(
                            navController = navController,
                            startDestination = TodoAppRouteList
                        ) {
                            composable<TodoAppRouteList> {
                                TodoListScreen(
                                    generalItemList = todoList,
                                    timelineItemList = todoList,
                                    setting = setting,
                                    onCheck = { item, checked ->
                                        todoListViewModel.check(
                                            item,
                                            checked
                                        )
                                    },
                                    onEdit = { item ->
                                        toggleOptions = false
                                        todoFormViewModel.loadTodoItemToEditForm(item.id)
                                        navController.navigate(TodoAppRouteEditOrNewForm(todoId = item.id))
                                    },
                                    onDelete = { item -> todoListViewModel.delete(item) },
                                )
                            }
                            composable<TodoAppRouteEditOrNewForm> { navStackEntry ->
                                TodoForm(
                                    item = todoFormViewModel.todoEditModel.value,
                                    onSave = { item ->
                                        navController.navigate(TodoAppRouteList)
                                        todoFormViewModel.save(item) { savedItem ->
                                            reminderService.schedule(savedItem)
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        this.checkSystemSettings()
    }

    override fun onRestart() {
        super.onRestart()
        this.checkSystemSettings()
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
                settingRepository.saveData(
                    setting.copy(
                        notificationAllowed = notificationAllowed,
                        reminderAllowed = reminderAllowed
                    )
                )
                this.cancel()
            }
        }
    }


}



