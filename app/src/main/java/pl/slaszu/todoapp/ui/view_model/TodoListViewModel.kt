package pl.slaszu.todoapp.ui.view_model

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
class TodoListViewModel @Inject constructor(
    private val todoRepository: TodoRepository<TodoModel>,
    private val settingRepository: SettingRepository,
    private val sortService: PresentationService
) : ViewModel() {

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

    fun check(item: TodoModel, checked: Boolean) {
        this.viewModelScope.launch {
            todoRepository.save(
                item.copy(
                    "done" to checked
                )
            )
        }
    }

    fun delete(item: TodoModel) {
        this.viewModelScope.launch {
            todoRepository.delete(item)
        }
    }

}