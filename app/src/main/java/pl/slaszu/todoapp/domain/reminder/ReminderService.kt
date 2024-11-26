package pl.slaszu.todoapp.domain.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.utils.toEpochMillis

class ReminderService(
    private val context: Context
) {

    @SuppressLint("ScheduleExactAlarm")
    fun schedule(item: TodoModel) {

        requireNotNull(item.id)
        requireNotNull(item.startDate)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExact(
            AlarmManager.RTC_WAKEUP,
            item.startDate!!.toEpochMillis(),
            this.createPendingIntent(item)
        )
    }

    fun cancel(item: TodoModel) {

        requireNotNull(item.id)
        requireNotNull(item.startDate)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(
            this.createPendingIntent(item)
        )
    }

    private fun createPendingIntent(item: TodoModel): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            item.id.toInt(),
            Intent(context, ReminderReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}