package pl.slaszu.todoapp.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.SettingRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingDataStorageRepository @Inject constructor(
    private val dataStorage: DataStore<Setting>
) : SettingRepository {
    override fun getData(): Flow<Setting> {
        return this.dataStorage.data
    }

    override suspend fun saveData(data: Setting) {
        this.dataStorage.updateData { data }
    }
}