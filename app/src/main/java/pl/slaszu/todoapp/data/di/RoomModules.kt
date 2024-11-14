package pl.slaszu.todoapp.data.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.slaszu.todoapp.data.room.AppDatabase
import pl.slaszu.todoapp.data.room.TodoModelDao
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
        ).build()
    }
}
