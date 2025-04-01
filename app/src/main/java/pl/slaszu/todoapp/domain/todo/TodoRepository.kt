package pl.slaszu.todoapp.domain.todo

import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

interface TodoRepository<T: TodoModel> {
    fun getTodoList(search:String?): Flow<List<T>>

    suspend fun getOnlyActiveTimeline(): Array<T>

    suspend fun getByDate(date: LocalDateTime): Array<T>

    suspend fun getById(id: Long): T?

    suspend fun save(todoItem: T): T

    suspend fun delete(todoItem: T)
}