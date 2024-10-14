package pl.slaszu.todoapp.data

import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class TodoSimpleRepository @Inject constructor(): TodoRepository {
    override fun getTodoList(): List<TodoModel> {
        return List<TodoModel>(20) { i ->
            TodoModel(
                "Todo item "+ Random.nextInt()
            )
        }
    }
}