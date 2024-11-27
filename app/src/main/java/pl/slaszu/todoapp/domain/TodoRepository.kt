package pl.slaszu.todoapp.domain

import kotlinx.coroutines.flow.Flow

interface TodoRepository<T:TodoModel> {
    fun getTodoList(): Flow<List<T>>

    suspend fun getById(id: Long): T?

    suspend fun save(todoItem: T): T

    suspend fun delete(todoItem: T)
}