package pl.slaszu.todoapp.data.room

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
    fun loadTodoById(id: Int): Flow<TodoModelEntity?>

    @Upsert
    suspend fun upsert(todoItem: TodoModelEntity)

    @Delete
    suspend fun delete(todoItem: TodoModelEntity)
}