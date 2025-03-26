package pl.slaszu.todoapp.domain.notification

import android.content.Context
import android.content.Intent
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class NotificationPermissionLauncher @Inject constructor(
    private val notificationPermissionService: NotificationPermissionService,
    @ApplicationContext private val context: Context
) {
    private fun getIntent(): Intent {
        return Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
        }
    }

    fun registerStartActivityLauncher(
        activity: ComponentActivity,
        callback: (Boolean) -> Unit
    ): ActivityResultLauncher<Intent> {
        return activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            callback(notificationPermissionService.hasPermission())
        }
    }

    fun registerRequestPermissionLauncher(
        activity: ComponentActivity,
        callback: (Boolean) -> Unit
    ): ActivityResultLauncher<String>? {

        if (ActivityCompat.shouldShowRequestPermissionRationale(
            activity,
            this.notificationPermissionService.permission
        )) {
            return null
        }

        return activity.registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            callback(it)
        }
    }

    @JvmName("launchIntent")
    fun launch(launcher: ActivityResultLauncher<Intent>) {
        launcher.launch(this.getIntent())
    }

    @JvmName("launchString")
    fun launch(launcher: ActivityResultLauncher<String>) {
        launcher.launch(this.notificationPermissionService.permission)
    }
}