package pl.slaszu.todoapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import pl.slaszu.todoapp.data.room.TodoModelDao
import pl.slaszu.todoapp.data.room.TodoModelEntity
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRoomRepository @Inject constructor(
    private val dao: TodoModelDao
) : TodoRepository<TodoModelEntity> {

    override fun getTodoList(): Flow<List<TodoModelEntity>> {
        return this.dao.loadTodoList()
    }

    override fun getById(id: Long): Flow<TodoModelEntity?> {
        return this.dao.loadTodoById(id)
    }

    override suspend fun save(todoItem: TodoModelEntity): TodoModelEntity =
        withContext(Dispatchers.IO) {
            val id = dao.upsert(todoItem)
            return@withContext todoItem.takeIf { id < 0 } ?: todoItem.copy(id = id)
        }


    override suspend fun delete(todoItem: TodoModelEntity) {
        withContext(Dispatchers.IO) {
            dao.delete(todoItem)
        }
    }
}

