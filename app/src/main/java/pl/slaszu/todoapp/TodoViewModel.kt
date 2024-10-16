package pl.slaszu.todoapp

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.FavoriteData
import pl.slaszu.todoapp.domain.FavoriteRepository
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val favoriteRepository: FavoriteRepository
) : ViewModel() {

    private val tasksUiModelFlow = combine(
        todoRepository.getTodoList(),
        favoriteRepository.getData()
    ) { todoList: List<TodoModel>, favorite: FavoriteData ->
        todoList.forEach { todo ->
            if (todo.id == favorite.favorite.toInt()) {
                todo.done = true
            }

        }
        Log.d("myapp", todoList.joinToString {
            "${it.id} ${it.text} [${it.done}]"
        })
        return@combine todoList
    }

    val todoList = tasksUiModelFlow

    fun toggleDone(item: TodoModel, checked: Boolean) {
        this.viewModelScope.launch {
            val favorite = FavoriteData(item.id.toString())
            favoriteRepository.saveData(favorite)
            Log.d("myapp", favorite.toString())
        }
    }
}