package pl.slaszu.todoapp.infrastructure.reminder

import android.app.AlarmManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import pl.slaszu.todoapp.domain.reminder.ReminderPermissionService

@RequiresApi(Build.VERSION_CODES.S)
class SystemReminderPermissionServiceService(
    private val context: Context
) : ReminderPermissionService {

    override fun hasPermission(): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return alarmManager.canScheduleExactAlarms()
    }
}