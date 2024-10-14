package pl.slaszu.todoapp

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository
) : ViewModel() {
    private val _todoList = todoRepository.getTodoList().toMutableStateList()
    val todoList: List<TodoModel>
        get() = _todoList

    fun toggleDone(item: TodoModel, checked: Boolean) {
        _todoList.find { it -> it == item }?.apply {
            done = checked
        }
        Log.d(
            "myApp", "${item.text} change to $checked"
        )
    }
}