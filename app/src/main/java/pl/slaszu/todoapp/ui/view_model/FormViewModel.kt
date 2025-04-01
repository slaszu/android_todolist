package pl.slaszu.todoapp.ui.view_model

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.R
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.reminder.exact.ReminderExactService
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.todo.TodoModelFactory
import pl.slaszu.todoapp.domain.todo.TodoRepository
import javax.inject.Inject

@HiltViewModel
class FormViewModel @Inject constructor(
    private val todoRepository: TodoRepository<TodoModel>,
    private val todoModelFactory: TodoModelFactory<TodoModel>,
    private val presentationService: PresentationService,
    private val reminderExactService: ReminderExactService
) : ViewModel() {

    var todoEditModel = mutableStateOf<TodoModel?>(null)
        private set

    fun loadTodoItemToEditForm(id: Long) {
        if (id == 0.toLong()) {
            todoEditModel.value = todoModelFactory.createDefault()
            return
        }

        this.viewModelScope.launch {
            val item = todoRepository.getById(id)
            todoEditModel.value = item ?: todoModelFactory.createDefault()
        }
    }

    fun save(item: TodoModel, snackbarHostState: SnackbarHostState) {

        this.viewModelScope.launch {

            if (todoEditModel.value == item) {
                snackbarHostState.showSnackbar(
                    message = presentationService.getStringResource(R.string.todo_form_no_change),
                    duration = SnackbarDuration.Short,
                    withDismissAction = true
                )
                return@launch
            }

            val savedItem = todoRepository.save(item)
            reminderExactService.schedule(savedItem)
            snackbarHostState.showSnackbar(
                message = presentationService.getStringResource(R.string.todo_form_saved),
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
        }
    }
}