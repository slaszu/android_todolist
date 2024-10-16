package pl.slaszu.todoapp.data

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow
import pl.slaszu.todoapp.domain.FavoriteData
import pl.slaszu.todoapp.domain.FavoriteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FavoriteDataStorageRepository @Inject constructor(
    private val dataStorage: DataStore<FavoriteData>
) : FavoriteRepository {
    override fun getData(): Flow<FavoriteData> {
        return this.dataStorage.data
    }

    override suspend fun saveData(data: FavoriteData) {
        this.dataStorage.updateData { data }
    }
}