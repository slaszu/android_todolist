package pl.slaszu.todoapp.domain.notification

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import dagger.hilt.android.qualifiers.ActivityContext
import javax.inject.Inject

class NotificationPermissionService @Inject constructor(
    @ActivityContext
    private val context: Context
) {
    val permission = Manifest.permission.POST_NOTIFICATIONS

    fun hasPermission(): Boolean =
        ActivityCompat.checkSelfPermission(
            context,
            this.permission
        ) == PackageManager.PERMISSION_GRANTED
}