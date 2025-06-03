package pl.slaszu.todoapp.domain.reminder.exact

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import pl.slaszu.todoapp.domain.reminder.ReminderPermissionService
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.utils.getUniqueInt
import pl.slaszu.todoapp.domain.utils.isTimeSet
import pl.slaszu.todoapp.domain.utils.toEpochMillis
import javax.inject.Inject

class ReminderExactService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val reminderPermissionService: ReminderPermissionService
) {
    @SuppressLint("ScheduleExactAlarm")
    fun schedule(item: TodoModel) {

        if (!reminderPermissionService.hasPermission()) {
            Log.d("myapp", "No permission for reminders!")
            return
        }

        if (item.startDate == null || !item.startDate!!.isTimeSet()) {
            return this.cancel(item)
        }

        if (item.done) {
            return this.cancel(item)
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            item.startDate!!.toEpochMillis(),
            this.createPendingIntent(item)
        )
        Log.d("myapp", "Schedule SET: $item")
    }

    fun cancel(item: TodoModel) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(
            this.createPendingIntent(item)
        )
        Log.d("myapp", "Schedule CANCEL: $item")
    }

    private fun createPendingIntent(item: TodoModel): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            item.getUniqueInt(),
            Intent(context, ReminderExactReceiver::class.java).apply {
                putExtra("ITEM_ID", item.id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}