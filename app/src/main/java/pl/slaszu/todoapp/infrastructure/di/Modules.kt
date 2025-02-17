package pl.slaszu.todoapp.infrastructure.di

import android.content.Context
import android.os.Build
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
import pl.slaszu.todoapp.domain.Setting
import pl.slaszu.todoapp.domain.SettingRepository
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoModelFactory
import pl.slaszu.todoapp.domain.TodoRepository
import pl.slaszu.todoapp.domain.reminder.ReminderPermission
import pl.slaszu.todoapp.infrastructure.SettingDataStorageRepository
import pl.slaszu.todoapp.infrastructure.TodoRoomRepository
import pl.slaszu.todoapp.infrastructure.reminder.FakeReminderPermissionService
import pl.slaszu.todoapp.infrastructure.reminder.ReminderPermissionService
import pl.slaszu.todoapp.infrastructure.room.TodoModelDao
import pl.slaszu.todoapp.infrastructure.room.TodoModelEntityFactory
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class Binds {
    @Binds
    abstract fun getFavoriteRepository(repo: SettingDataStorageRepository): SettingRepository
}

@InstallIn(SingletonComponent::class)
@Module
object AndroidVersionProviders {

    @Provides
    @Singleton
    fun getReminderPermissionService(@ApplicationContext context: Context): ReminderPermission {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return FakeReminderPermissionService()
        }
        return ReminderPermissionService(context)
    }
}

@InstallIn(SingletonComponent::class)
@Module
object TodoModelProviders {

    @Suppress("UNCHECKED_CAST")
    @Provides
    @Singleton
    fun getTodoRepository(dao: TodoModelDao): TodoRepository<TodoModel> {
        return TodoRoomRepository(dao) as TodoRepository<TodoModel>
    }

    @Suppress("UNCHECKED_CAST")
    @Provides
    @Singleton
    fun getTodoModelFactory(): TodoModelFactory<TodoModel> {
        return TodoModelEntityFactory() as TodoModelFactory<TodoModel>
    }

}

@InstallIn(SingletonComponent::class)
@Module
object SettingProviders {

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