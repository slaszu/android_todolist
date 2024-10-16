package pl.slaszu.todoapp.data.di

import android.content.Context
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.slaszu.todoapp.data.FavoriteDataStorageRepository
import pl.slaszu.todoapp.data.TodoSimpleRepository
import pl.slaszu.todoapp.domain.FavoriteData
import pl.slaszu.todoapp.domain.FavoriteRepository
import pl.slaszu.todoapp.domain.TodoRepository
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class Binds {

    @Binds
    abstract fun getTodoRepository(repo: TodoSimpleRepository): TodoRepository

    @Binds
    abstract fun getFavoriteRepository(repo: FavoriteDataStorageRepository): FavoriteRepository
}

@InstallIn(SingletonComponent::class)
@Module
object Providers {

    @Provides
    @Singleton
    fun getDataStorageFavoriteData(@ApplicationContext appContext: Context): DataStore<FavoriteData> {
        return DataStoreFactory.create(
            serializer = object : Serializer<FavoriteData> {
                override val defaultValue: FavoriteData = FavoriteData()

                override suspend fun readFrom(input: InputStream): FavoriteData {
                    try {
                        return Json.decodeFromString<FavoriteData>(
                            input.readBytes().decodeToString()
                        )
                    } catch (serialization: SerializationException) {
                        throw CorruptionException("Unable to read UserPrefs", serialization)
                    }
                }

                override suspend fun writeTo(t: FavoriteData, output: OutputStream) {
                    output.write(Json.encodeToString(t).encodeToByteArray())
                }
            },
            produceFile = {
                appContext.dataStoreFile("data_store_favorite.pb")
            }
        )
    }

}