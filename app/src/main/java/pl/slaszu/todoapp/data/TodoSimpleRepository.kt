package pl.slaszu.todoapp.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TodoSimpleRepository @Inject constructor() : TodoRepository {
    override fun getTodoList(): Flow<List<TodoModel>> {
        return flow {
            emit(List(20) { i ->
                TodoModel(id = i, text = "Todo item number $i")
            })
        }
    }
}