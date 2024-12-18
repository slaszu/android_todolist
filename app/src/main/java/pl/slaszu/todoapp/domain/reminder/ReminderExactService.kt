package pl.slaszu.todoapp.domain.reminder

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.utils.isTimeSet
import pl.slaszu.todoapp.domain.utils.toEpochMillis

class ReminderExactService(
    private val context: Context
) {


    @SuppressLint("ScheduleExactAlarm")
    fun schedule(item: TodoModel) {

        if (item.id == 0L) {
            Log.d("myapp", "Item has no id: $item")
            return
        }

        if (item.startDate == null || !item.startDate!!.isTimeSet()) {
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

    private fun cancel(item: TodoModel) {

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(
            this.createPendingIntent(item)
        )
        Log.d("myapp", "Schedule CANCEL: $item")
    }

    private fun createPendingIntent(item: TodoModel): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, ReminderExactReceiver::class.java).apply {
                putExtra("ITEM_ID", item.id)
            },
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
}