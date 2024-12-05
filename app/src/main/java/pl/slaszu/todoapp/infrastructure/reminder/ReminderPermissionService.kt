package pl.slaszu.todoapp.infrastructure.reminder

import android.Manifest
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import pl.slaszu.todoapp.domain.reminder.ReminderPermission

@RequiresApi(Build.VERSION_CODES.S)
class ReminderPermissionService(
    private val context: Context
) : ReminderPermission {

    private val permission = Manifest.permission.SCHEDULE_EXACT_ALARM

    override fun hasPermission(): Boolean {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        return alarmManager.canScheduleExactAlarms()
    }

    override fun openSettingActivity() {
        val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.parse("package:${context.packageName}")
            //putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
        }
        context.startActivity(intent)
    }
}