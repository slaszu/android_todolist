package pl.slaszu.todoapp.domain.notification.action

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationManagerCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.runBlocking
import pl.slaszu.todoapp.domain.todo.TodoManager
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.todo.TodoRepository
import javax.inject.Inject

@AndroidEntryPoint
class NotificationFinishActionReceiver : BroadcastReceiver() {

    @Inject
    lateinit var todoManager: TodoManager

    @Inject
    lateinit var todoRepository: TodoRepository<TodoModel>

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("myapp", "Receiver")

        if (context == null || intent == null) {
            Log.d("myapp", "Receiver: early return")
            return
        }

        val items = getItemsFromIntend(intent)
        val notificationId = getNotificationIdFromIntend(intent)

        Log.d("myapp", "Receiver: notificationId = $notificationId")
        Log.d("myapp", "Receiver: items = ${items.toList()}")

        runBlocking {
            items.forEach { todoId ->
                todoRepository.getById(todoId).run {
                    if (this != null) {
                        val finishTodo = this.finishCopy() ?: return@forEach
                        todoManager.save(finishTodo)
                        Log.d("myapp", "Receiver: finishTodo = $finishTodo")
                    }
                }
            }
        }

        val notificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManagerCompat.cancel(notificationId)
    }

    companion object {
        const val ITEMS_EXTRAS = "items_extras"
        const val NOTIFICATION_ID_EXTRAS = "notification_id_extras"

        fun getItemsFromIntend(intent: Intent): Array<String> =
            intent.getStringArrayExtra(ITEMS_EXTRAS) ?: arrayOf()

        fun getNotificationIdFromIntend(intent: Intent): Int =
            intent.getIntExtra(NOTIFICATION_ID_EXTRAS, 0)

    }
}