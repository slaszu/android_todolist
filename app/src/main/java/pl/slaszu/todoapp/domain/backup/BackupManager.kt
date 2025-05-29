package pl.slaszu.todoapp.domain.backup

import android.util.Log
import pl.slaszu.todoapp.domain.auth.UserService
import pl.slaszu.todoapp.domain.todo.TodoModel
import javax.inject.Inject

class BackupManager @Inject constructor(
    private val backupRepository: BackupRepository,
    private val userService: UserService
) {

    fun addOrUpdateItem(item: TodoModel) {
        val user = userService.getUserOrNull()
        if (user == null) {
            Log.d("myapp", "Backup unavailable: user is null")
            return
        }
        backupRepository.saveItem(item, user)
    }

    fun deleteItem(item: TodoModel) {
        val user = userService.getUserOrNull()
        if (user == null) {
            Log.d("myapp", "Backup unavailable: user is null")
            return
        }
        backupRepository.delItem(item, user)
    }
}