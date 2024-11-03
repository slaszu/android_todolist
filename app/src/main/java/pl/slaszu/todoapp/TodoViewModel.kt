package pl.slaszu.todoapp

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.PresentationService
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.SettingRepository
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val settingRepository: SettingRepository,
    private val sortService: PresentationService
) : ViewModel() {

    var todoEditModel = mutableStateOf<TodoModel?>(null)
        private set

    val todoListFlow = combine(
        todoRepository.getTodoList(),
        settingRepository.getData()
    ) { todoList: List<TodoModel>, setting: Setting ->
        sortService.process(todoList, setting)
    }

    val settingFlow = settingRepository.getData()

    fun saveSetting(setting: Setting) {
        this.viewModelScope.launch {
            settingRepository.saveData(setting)
        }
    }

    fun loadTodoItemToEditForm(id: Int?) {

        if (id == null) {
            todoEditModel.value = TodoModel()
            return
        }

        Log.d("myapp", "loadTodoEditModel")
        this.viewModelScope.launch {
            todoRepository.getById(id).collect { item ->
                todoEditModel.value = item ?: TodoModel()
                Log.d("myapp", "vieModel->load: ${item.toString()}")
            }
        }
    }

    fun check(item: TodoModel, checked: Boolean) {
        this.viewModelScope.launch {
            todoRepository.save(
                item.copy(
                    done = checked
                )
            )
        }
    }

    fun delete(item: TodoModel) {
        this.viewModelScope.launch {
            todoRepository.delete(item)
        }
    }

    fun save(item: TodoModel) {
        this.viewModelScope.launch {
            todoRepository.save(item)
        }
    }
}