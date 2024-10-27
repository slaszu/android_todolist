package pl.slaszu.todoapp.domain

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.coroutines.flow.Flow


@Entity(tableName = "todo")
data class TodoModel(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    @ColumnInfo(name = "text") val text: String = "",
    @ColumnInfo(name = "done") val done: Boolean = false,
    @ColumnInfo(name = "priority") val priority: Int = 0,
)

interface TodoRepository {
    fun getTodoList(): Flow<List<TodoModel>>

    fun getById(id:Int): Flow<TodoModel?>

    suspend fun save(todoItem: TodoModel)

    suspend fun delete(todoItem: TodoModel)
}