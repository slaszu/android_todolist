package pl.slaszu.todoapp.infrastructure.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TodoModelDao {
    @Query("SELECT * from todo ORDER BY start_date ASC")
    fun loadTodoList(): Flow<List<TodoModelEntity>>

    @Query("SELECT * from todo where id = :id")
    suspend fun loadTodoById(id: Long): TodoModelEntity?

    @Query("SELECT * from todo where start_date = :date")
    suspend fun loadTodoByDate(date: LocalDateTime): Array<TodoModelEntity>

    @Upsert
    suspend fun upsert(todoItem: TodoModelEntity): Long

    @Delete
    suspend fun delete(todoItem: TodoModelEntity)
}