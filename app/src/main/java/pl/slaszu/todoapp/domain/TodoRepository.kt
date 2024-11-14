package pl.slaszu.todoapp.domain

import kotlinx.coroutines.flow.Flow

interface TodoRepository<T:TodoModel> {
    fun getTodoList(): Flow<List<T>>

    fun getById(id: Int): Flow<T?>

    suspend fun save(todoItem: T)

    suspend fun delete(todoItem: T)
}