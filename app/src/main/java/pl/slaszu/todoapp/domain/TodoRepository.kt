package pl.slaszu.todoapp.domain

import kotlinx.coroutines.flow.Flow

interface TodoRepository<T:TodoModel> {
    fun getTodoList(): Flow<List<T>>

    fun getById(id: Long): Flow<T?>

    suspend fun save(todoItem: T): T

    suspend fun delete(todoItem: T)
}