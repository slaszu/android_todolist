package pl.slaszu.todoapp.infrastructure.di

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import androidx.datastore.dataStoreFile
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pl.slaszu.todoapp.domain.auth.UserService
import pl.slaszu.todoapp.domain.backup.BackupRepository
import pl.slaszu.todoapp.domain.reminder.ReminderPermissionService
import pl.slaszu.todoapp.domain.setting.Setting
import pl.slaszu.todoapp.domain.setting.SettingRepository
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.todo.TodoModelFactory
import pl.slaszu.todoapp.domain.todo.TodoRepository
import pl.slaszu.todoapp.infrastructure.SettingDataStorageRepository
import pl.slaszu.todoapp.infrastructure.TodoRoomRepository
import pl.slaszu.todoapp.infrastructure.firebase.auth.FirebaseBackupRepository
import pl.slaszu.todoapp.infrastructure.firebase.auth.FirebaseUserService
import pl.slaszu.todoapp.infrastructure.reminder.FakeReminderPermissionServiceService
import pl.slaszu.todoapp.infrastructure.reminder.SystemReminderPermissionServiceService
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

    @Binds
    abstract fun getUserService(service: FirebaseUserService): UserService
}

@InstallIn(SingletonComponent::class)
@Module
object AppProviders {

    @Provides
    @Singleton
    fun getBackupRepository(): BackupRepository {

        return FirebaseBackupRepository(Firebase.firestore)
    }

    @Provides
    @Singleton
    fun getReminderPermissionService(@ApplicationContext context: Context): ReminderPermissionService {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
            return FakeReminderPermissionServiceService()
        }
        return SystemReminderPermissionServiceService(context)
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
                            serialization.message ?: "SerializationException but no message :/"
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