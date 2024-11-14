package pl.slaszu.todoapp.data

import kotlinx.coroutines.flow.Flow
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

    override fun getById(id: Int): Flow<TodoModelEntity?> {
        return this.dao.loadTodoById(id)
    }

    override suspend fun save(todoItem: TodoModelEntity) {
        this.dao.upsert(todoItem)
    }

    override suspend fun delete(todoItem: TodoModelEntity) {
        this.dao.delete(todoItem)
    }
}

