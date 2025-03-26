package pl.slaszu.todoapp.domain.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.utils.toEpochMillis
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class ReminderRepeatService @Inject constructor(
    @ApplicationContext val context: Context
) {
    fun scheduleRepeatOnePerDay(setting: Setting) {
        this.scheduleRepeatOnePerDay(setting.reminderRepeatHour, setting.reminderRepeatMinute)
    }

    private fun scheduleRepeatOnePerDay(hour: Int, minute: Int) {
        val dateToSet = this.getLocaleDateTimeForNextDayWithHour(hour, minute)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            dateToSet.toEpochMillis(),
            AlarmManager.INTERVAL_DAY,
            this.createPendingIntent()
        )
        Log.d("myapp", "Schedule repeat SET: time $dateToSet")
    }

    private fun createPendingIntent(): PendingIntent {
        return PendingIntent.getBroadcast(
            context,
            0,
            Intent(context, ReminderRepeatReceiver::class.java),
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun getLocaleDateTimeForNextDayWithHour(hour: Int, minute: Int): LocalDateTime {

        val now = LocalDateTime.now(ZoneId.systemDefault())
        return now.withHour(hour).withMinute(minute).let {
            if (now > it) {
                it.plusDays(1)
            } else {
                it
            }
        }
    }
}