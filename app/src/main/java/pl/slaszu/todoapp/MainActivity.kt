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
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import pl.slaszu.todoapp.domain.notification.NotificationPermissionService
import pl.slaszu.todoapp.domain.notification.NotificationService
import pl.slaszu.todoapp.domain.reminder.ReminderExactService
import pl.slaszu.todoapp.domain.reminder.ReminderPermission
import pl.slaszu.todoapp.domain.reminder.ReminderRepeatService
import pl.slaszu.todoapp.domain.utils.clearTime
import pl.slaszu.todoapp.ui.element.form.TodoForm
import pl.slaszu.todoapp.ui.element.list.TodoFloatingActionButton
import pl.slaszu.todoapp.ui.element.list.TodoListScreen
import pl.slaszu.todoapp.ui.element.list.TopBar
import pl.slaszu.todoapp.ui.element.remiander.ReminderDialog
import pl.slaszu.todoapp.ui.element.setting.SettingScreen
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteEditOrNewForm
import pl.slaszu.todoapp.ui.navigation.TodoAppRouteList
import pl.slaszu.todoapp.ui.navigation.TodoAppSetting
import pl.slaszu.todoapp.ui.theme.TodoAppTheme
import pl.slaszu.todoapp.ui.view_model.FormViewModel
import pl.slaszu.todoapp.ui.view_model.ListViewModel
import java.time.LocalDateTime
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

        setContent {
            val navController = rememberNavController()
            val listViewModel: ListViewModel = viewModel()
            val formViewModel: FormViewModel = viewModel()

            val setting =
                listViewModel.settingFlow.collectAsStateWithLifecycle(Setting()).value
            val todoList =
                listViewModel.todoListFlow.collectAsStateWithLifecycle(emptyList()).value

            TodoAppTheme {
                Scaffold(
                    topBar = {
                        TopBar(
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
                            startDestination = TodoAppRouteList
                        ) {
                            composable<TodoAppRouteList> {
                                Column {
                                    TodoListScreen(
                                        generalItemList = todoList.filter {
                                            it.startDate == null
                                        },
                                        timelineItemList = todoList.filter {
                                            it.startDate != null
                                        }.let {
                                            listViewModel.convertToTimeline(it)
                                        },
                                        setting = setting,
                                        onCheck = { item, checked ->
                                            listViewModel.check(
                                                item,
                                                checked
                                            )
                                        },
                                        onEdit = { item ->
                                            formViewModel.loadTodoItemToEditForm(item.id)
                                            navController.navigate(TodoAppRouteEditOrNewForm)
                                        },
                                        onDelete = { item -> listViewModel.delete(item) },
                                    )
                                }
                            }
                            composable<TodoAppRouteEditOrNewForm> {
                                TodoForm(
                                    item = formViewModel.todoEditModel.value,
                                    onSave = { item ->
                                        navController.navigate(TodoAppRouteList)
                                        formViewModel.save(item) { savedItem ->
                                            reminderExactService.schedule(savedItem)
                                        }
                                    }
                                )
                            }
                            composable<TodoAppSetting> {
                                SettingScreen(
                                    setting = setting,
                                    onChange = {
                                        listViewModel.saveSetting(it)
                                    },
                                    onReminderClick = { reminderPermissionService.openSettingActivity() },
                                    onNotificationClick = { notificationPermissionService.openSettingActivity() }
                                )
                            }
                        }
                    }

                    val reminderItemsId = getReminderItemIds()
                    ReminderDialog(
                        items = todoList.filter {
                            reminderItemsId.contains(it.id)
                        },
                        onCloseItem = { item ->
                            listViewModel.check(
                                item,
                                true
                            )
                        }
                    )


                }
            }
        }

        this.checkSystemSettings()
    }

    private fun getReminderItemIds(): LongArray {
        return this.intent.getLongArrayExtra(NotificationService.INTENT_KEY) ?: longArrayOf(1, 2, 3)

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
                val refreshSetting = setting.copy(
                    notificationAllowed = notificationAllowed,
                    reminderAllowed = reminderAllowed
                )

                reminderRepeatService.scheduleRepeatOnePerDay(refreshSetting.reminderRepeatHour)

                val itemArray = repository.getByDate(LocalDateTime.now().clearTime())
                Log.d("myapp", "Items : ${itemArray.size}")

                settingRepository.saveData(refreshSetting)
                this.cancel()
            }
        }

    }


}



