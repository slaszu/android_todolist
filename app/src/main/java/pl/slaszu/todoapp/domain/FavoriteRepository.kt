package pl.slaszu.todoapp.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable

@Serializable
data class FavoriteData(
    val favorite: String = "3"
)

interface FavoriteRepository {
    fun getData(): Flow<FavoriteData>
    suspend fun saveData(data: FavoriteData)
}