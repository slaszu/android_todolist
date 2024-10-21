package pl.slaszu.todoapp.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Dao
interface TodoModelDao {
    @Query("SELECT * from todo ORDER BY priority")
    fun loadTodoList(): Flow<List<TodoModel>>

    @Upsert
    suspend fun upsert(todoItem: TodoModel)

    @Delete
    suspend fun delete(todoItem: TodoModel)
}

@Singleton
class TodoRoomRepository @Inject constructor(
    private val dao: TodoModelDao
) : TodoRepository {


    override fun getTodoList(): Flow<List<TodoModel>> {
        return this.dao.loadTodoList()
    }

    override suspend fun save(todoItem: TodoModel) {
        this.dao.upsert(todoItem)
    }

    override suspend fun delete(todoItem: TodoModel) {
        this.dao.delete(todoItem)
    }
}

