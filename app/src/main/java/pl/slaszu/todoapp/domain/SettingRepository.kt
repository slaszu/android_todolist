package pl.slaszu.todoapp.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class Setting(
    val showDone: Boolean = true,
    val notificationAllowed: Boolean = false,
    val reminderAllowed: Boolean = false,
    val tabSelectedIndex: Int = 0,
    val reminderRepeatHour: Int = 8,
    val reminderRepeatMinute: Int = 0,
)

interface SettingRepository {
    fun getData(): Flow<Setting>
    suspend fun saveData(data: Setting)
}