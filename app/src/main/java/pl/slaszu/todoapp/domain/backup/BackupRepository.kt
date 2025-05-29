package pl.slaszu.todoapp.domain.backup

import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.todo.TodoModel

interface BackupRepository {
    fun saveItem(item: TodoModel, user: User)
    fun delItem(item: TodoModel, user: User)
    fun getItem(id: Long, user: User)
    fun recreate(items: Array<TodoModel>, user: User)
}