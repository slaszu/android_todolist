package pl.slaszu.todoapp.ui.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoModelFactory
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject

@HiltViewModel
class TodoFormViewModel @Inject constructor(
    private val todoRepository: TodoRepository<TodoModel>,
    private val todoModelFactory: TodoModelFactory<TodoModel>
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

    fun save(item: TodoModel, callback: (TodoModel) -> Unit) {
        this.viewModelScope.launch {
            val savedItem = todoRepository.save(item)
            callback(savedItem)
        }
    }
}