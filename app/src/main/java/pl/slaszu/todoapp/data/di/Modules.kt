package pl.slaszu.todoapp.data.di

import android.content.Context
import android.util.Log
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
import pl.slaszu.todoapp.data.SettingDataStorageRepository
import pl.slaszu.todoapp.data.TodoRoomRepository
import pl.slaszu.todoapp.data.room.TodoModelDao
import pl.slaszu.todoapp.data.room.TodoModelEntityFactory
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.SettingRepository
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoModelFactory
import pl.slaszu.todoapp.domain.TodoRepository
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class Binds {

//    @Binds
//    abstract fun getTodoRepository(repo: TodoRoomRepository): TodoRepository<TodoModel>

//    @Binds
//    abstract fun getTodoModelFactory(factory: TodoModelEntityFactory): TodoModelFactory<*>

    @Binds
    abstract fun getFavoriteRepository(repo: SettingDataStorageRepository): SettingRepository
}

@InstallIn(SingletonComponent::class)
@Module
object Providers {

    @Provides
    @Singleton
    fun getTodoRepository(dao: TodoModelDao): TodoRepository<TodoModel> {
        return TodoRoomRepository(dao) as TodoRepository<TodoModel>
    }

    @Provides
    @Singleton
    fun getTodoModelFactory(): TodoModelFactory<TodoModel> {
        return TodoModelEntityFactory() as TodoModelFactory<TodoModel>
    }

    @Provides
    @Singleton
    fun getDataStorageFavoriteData(@ApplicationContext appContext: Context): DataStore<Setting> {
        return DataStoreFactory.create(
            serializer = object : Serializer<Setting> {
                override val defaultValue: Setting = Setting()

                override suspend fun readFrom(input: InputStream): Setting {
                    try {
                        return Json.decodeFromString<Setting>(
                            input.readBytes().decodeToString()
                        )
                    } catch (serialization: SerializationException) {
                        Log.d(
                            "myapp",
                            serialization?.message ?: "SerializationException but no message :/"
                        )
                        //throw CorruptionException("Unable to read UserPrefs", serialization)
                        return defaultValue
                    }
                }

                override suspend fun writeTo(t: Setting, output: OutputStream) {
                    output.write(Json.encodeToString(t).encodeToByteArray())
                }
            },
            produceFile = {
                appContext.dataStoreFile("data_store_setting.pb")
            }
        )
    }

}