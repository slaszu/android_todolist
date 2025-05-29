package pl.slaszu.todoapp.infrastructure

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import pl.slaszu.todoapp.domain.todo.TodoRepository
import pl.slaszu.todoapp.infrastructure.room.TodoModelDao
import pl.slaszu.todoapp.infrastructure.room.TodoModelEntity
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoRoomRepository @Inject constructor(
    private val dao: TodoModelDao
) : TodoRepository<TodoModelEntity> {

    override fun getTodoList(search: String?): Flow<List<TodoModelEntity>> {
        if (search.isNullOrBlank()) {
            return this.dao.loadTodoList()
        } else {
            return this.dao.loadTodoList(search)
        }
    }

    override suspend fun getOnlyActiveTimeline(): Array<TodoModelEntity> =
        withContext(Dispatchers.IO) {
            return@withContext dao.loadTodoNotCloseWithDate()
        }

    override suspend fun getByDate(date: LocalDateTime): Array<TodoModelEntity> =
        withContext(Dispatchers.IO) {
            return@withContext dao.loadTodoByDate(date)
        }


    override suspend fun getById(id: Long): TodoModelEntity? =
        withContext(Dispatchers.IO) {
            return@withContext dao.loadTodoById(id)
        }

    override suspend fun getAll(): Array<TodoModelEntity> =
        withContext(Dispatchers.IO) {
            return@withContext dao.loadTodoListAll()
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

