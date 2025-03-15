package pl.slaszu.todoapp.ui.view_model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.SettingRepository
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import pl.slaszu.todoapp.domain.reminder.ReminderRepeatService
import javax.inject.Inject


@HiltViewModel
class ListViewModel @Inject constructor(
    private val todoRepository: TodoRepository<TodoModel>,
    private val settingRepository: SettingRepository,
    private val presentationService: PresentationService,
    private val reminderRepeatService: ReminderRepeatService
) : ViewModel() {

    fun getTodoList(search: String?): Flow<List<TodoModel>> {
        return todoRepository.getTodoList(search)
    }

    val settingFlow = settingRepository.getData()

    fun saveSetting(setting: Setting, oldSetting: Setting) {
        this.viewModelScope.launch {
            settingRepository.saveData(setting)

            if (oldSetting.reminderRepeatHour != setting.reminderRepeatHour ||
                oldSetting.reminderRepeatMinute != setting.reminderRepeatMinute
            ) {
                reminderRepeatService.scheduleRepeatOnePerDay(
                    hour = setting.reminderRepeatHour,
                    minute = setting.reminderRepeatMinute
                )
            }
        }
    }

    fun check(item: TodoModel, checked: Boolean, snackbarHostState: SnackbarHostState? = null) {
        this.viewModelScope.launch {

            /**
             * if :
             * - checked == true
             * - startDate != empty
             * - repeatType != null
             * then :
             * - add repeatType period to startDate
             * else :
             * - set done to cheked
             */

            var showSnackbar = true
            var itemToSave = item.copy()
            if (checked && item.startDate != null && item.repeatType != null) {
                itemToSave = itemToSave.copy(
                    "startDate" to item.repeatType!!.calculateNewDate(item.startDate!!)
                )
                showSnackbar = false
            } else {
                itemToSave = itemToSave.copy(
                    "done" to checked
                )
            }

            todoRepository.save(itemToSave)

            // snackbar only on close item
            if (!showSnackbar || snackbarHostState == null || !checked) return@launch

            val result = snackbarHostState.showSnackbar(
                message = "${presentationService.getStringResource(R.string.item_type_done)}: ${item.text}",
                actionLabel = presentationService.getStringResource(R.string.undo),
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    todoRepository.save(item)
                }

                SnackbarResult.Dismissed -> {
                    /* Handle snackbar dismissed */
                }
            }
        }
    }

    fun delete(item: TodoModel, snackbarHostState: SnackbarHostState? = null) {
        this.viewModelScope.launch {
            todoRepository.delete(item)

            if (snackbarHostState == null) return@launch

            val result = snackbarHostState.showSnackbar(
                message = "${presentationService.getStringResource(R.string.deleted)}: ${item.text}",
                actionLabel = presentationService.getStringResource(R.string.undo),
                duration = SnackbarDuration.Long,
                withDismissAction = true
            )
            when (result) {
                SnackbarResult.ActionPerformed -> {
                    todoRepository.save(item)
                }

                SnackbarResult.Dismissed -> {
                    /* Handle snackbar dismissed */
                }
            }
        }
    }

}