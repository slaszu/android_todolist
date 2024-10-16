package pl.slaszu.todoapp.domain

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.flow.Flow


class TodoModel(
    val id: Int,
    var text: String,
    initialChecked: Boolean = false
) {
    var done by mutableStateOf(initialChecked)
}

interface TodoRepository {
    fun getTodoList(): Flow<List<TodoModel>>
}