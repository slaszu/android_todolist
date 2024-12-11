package pl.slaszu.todoapp.domain.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import pl.slaszu.todoapp.domain.utils.toEpochMillis
import java.time.LocalDateTime
import java.time.ZoneId

class ReminderRepeatService(
    private val context: Context
) {
    fun scheduleRepeatOnePerDay(hour: Int) {
        val dateToSet = this.getLocaleDateTimeForNextDayWithHour(hour)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            dateToSet.toEpochMillis(),
            AlarmManager.INTERVAL_FIFTEEN_MINUTES,
            this.createPendingIntent()
        )
        Log.d("myapp", "Schedule repeat SET: time $dateToSet")
    }

    private fun createPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            999,
            Intent(context, ReminderRepeatReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getLocaleDateTimeForNextDayWithHour(hour: Int): LocalDateTime {
        return LocalDateTime.now(ZoneId.systemDefault()).plusMinutes(1)
//            .withMinute(0)
//            .withSecond(0)
//            .withNano(0)
//            .apply {
//                val actualHour = this.hour
//                var new = this.withHour(hour)
//                if (actualHour >= hour) {
//                    new = new.plusDays(1)
//                }
//                return new
//            }
    }
}