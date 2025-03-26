package pl.slaszu.todoapp.domain.reminder

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ReminderPermissionLauncher @Inject constructor(
    private val reminderPermissionService: ReminderPermissionService,
    @ApplicationContext val context: Context
) {
    private fun getIntent(): Intent {
        return Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
            data = Uri.parse("package:${context.packageName}")
        //    flags = FLAG_ACTIVITY_NEW_TASK
        }
    }

    fun registerStartActivityLauncher(
        activity: ComponentActivity,
        callback: (Boolean) -> Unit
    ): ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            callback(reminderPermissionService.hasPermission())
        }
    }

    @JvmName("launchIntent")
    fun launch(launcher: ActivityResultLauncher<Intent>) {
        launcher.launch(this.getIntent())
    }
}