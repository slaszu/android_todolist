package pl.slaszu.todoapp.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class Setting(
    val favorite: String = "3"
)

interface SettingRepository {
    fun getData(): Flow<Setting>
    suspend fun saveData(data: Setting)
}