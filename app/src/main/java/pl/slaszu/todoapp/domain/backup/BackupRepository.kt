package pl.slaszu.todoapp.domain.backup

import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.todo.TodoModel

interface BackupRepository {
    fun saveItem(item: TodoModel, user: User)
    fun delItem(item: TodoModel, user: User)
    suspend fun getAll(user: User): Array<TodoModel>
    fun exportAll(items: Array<TodoModel>, user: User)
}