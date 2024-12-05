package pl.slaszu.todoapp.domain.notification

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.provider.Settings
import androidx.core.app.ActivityCompat

class NotificationPermissionService(
    private val activity: Activity
) {
    companion object {
        const val NOTIFICATION_REQUEST_CODE = 1
    }

    private val permission = Manifest.permission.POST_NOTIFICATIONS

    fun hasPermission(): Boolean =
        ActivityCompat.checkSelfPermission(
            activity,
            this.permission
        ) == PackageManager.PERMISSION_GRANTED

    fun openSettingActivity() {
        val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
            putExtra(Settings.EXTRA_APP_PACKAGE, activity.packageName)
            //putExtra(Settings.EXTRA_CHANNEL_ID, CHANNEL_ID)
        }
        activity.startActivity(intent)
    }

    fun askPermissionRequest() {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(this.permission),
            NOTIFICATION_REQUEST_CODE
        )
    }

    fun isPermissionGranted(permissions: Array<String>, grantResults: IntArray): Boolean {
        permissions.forEachIndexed { index, s ->
            if (s == permission) {
                return grantResults[index] == PackageManager.PERMISSION_GRANTED
            }
        }
        return false;
    }
}