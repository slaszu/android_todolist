package pl.slaszu.todoapp.infrastructure.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.slaszu.todoapp.infrastructure.room.AppDatabase
import pl.slaszu.todoapp.infrastructure.room.TodoModelDao
import java.util.concurrent.Executors
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object RoomProviders {
    @Provides
    fun provideTodoModelDao(database: AppDatabase): TodoModelDao {
        return database.todoModelDao()
    }

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "todo_list.db"
        )
            .setQueryCallback(
                { sqlQuery, bindArgs ->
                    //Log.d("myapp", "SQL Query: $sqlQuery SQL Args: $bindArgs")
                },
                Executors.newSingleThreadExecutor()
            )
            .build()
    }
}
