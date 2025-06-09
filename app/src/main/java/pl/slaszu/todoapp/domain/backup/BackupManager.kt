package pl.slaszu.todoapp.domain.backup

import android.util.Log
import pl.slaszu.todoapp.domain.auth.UserService
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.todo.TodoRepository
import javax.inject.Inject

class BackupManager @Inject constructor(
    private val backupRepository: BackupRepository,
    private val todoRepository: TodoRepository<TodoModel>,
    private val userService: UserService
) {

    fun addOrUpdateItem(item: TodoModel) {
        val user = userService.getUserOrNull()
        if (user == null) {
            Log.d("myapp", "Backup save unavailable: user is null")
            return
        }
        backupRepository.saveItem(item, user)
    }

    fun deleteItem(item: TodoModel) {
        val user = userService.getUserOrNull()
        if (user == null) {
            Log.d("myapp", "Backup delete unavailable: user is null")
            return
        }
        backupRepository.delItem(item, user)
    }

    suspend fun importAll() {
        val user = userService.getUserOrNull()
        if (user == null) {
            Log.d("myapp", "Backup import unavailable: user is null")
            return
        }

        val todoIdList = todoRepository.getAll().map { it.id }
        backupRepository.getAll(user).filter {
            !todoIdList.contains(it.id)
        }.forEach {
            todoRepository.save(it)
        }
    }
}