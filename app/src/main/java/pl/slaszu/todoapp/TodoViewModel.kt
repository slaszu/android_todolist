package pl.slaszu.todoapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.SettingRepository
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val settingRepository: SettingRepository
) : ViewModel() {

//    private val todoListFlow2 = combine(
//        todoRepository.getTodoList(),
//        settingRepository.getData()
//    ) { todoList: List<TodoModel>, favorite: Setting ->
//        todoList.forEach { todo ->
//
//
//        }
//        return@combine todoList
//    }

    val todoListFlow = todoRepository.getTodoList()

    val settingFlow = settingRepository.getData()

    fun toggleDone(item: TodoModel, checked: Boolean) {
        this.viewModelScope.launch {
            var favorite = Setting(item.id.toString())
            if (!checked) {
                favorite = favorite.copy(favorite = "0")
            }
            settingRepository.saveData(favorite)
            Log.d("myapp", favorite.toString())
        }
    }

    fun save(item: TodoModel) {
        this.viewModelScope.launch {
            todoRepository.save(item)
        }
    }
}