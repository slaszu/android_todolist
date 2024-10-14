package pl.slaszu.todoapp.domain

interface TodoRepository {
    fun getTodoList(): List<TodoModel>
}