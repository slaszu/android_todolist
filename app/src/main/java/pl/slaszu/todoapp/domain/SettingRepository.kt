package pl.slaszu.todoapp.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class Setting(
    val showDone: Boolean = true,
    val notificationAllowed: Boolean = false
)

interface SettingRepository {
    fun getData(): Flow<Setting>
    suspend fun saveData(data: Setting)
}