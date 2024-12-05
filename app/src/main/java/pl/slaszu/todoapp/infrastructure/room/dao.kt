package pl.slaszu.todoapp.infrastructure.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoModelDao {
    @Query("SELECT * from todo ORDER BY priority")
    fun loadTodoList(): Flow<List<TodoModelEntity>>

    @Query("SELECT * from todo where id = :id")
    suspend fun loadTodoById(id: Long): TodoModelEntity?

    @Upsert
    suspend fun upsert(todoItem: TodoModelEntity): Long

    @Delete
    suspend fun delete(todoItem: TodoModelEntity)
}