package pl.slaszu.todoapp.data.di

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import pl.slaszu.todoapp.data.TodoModelDao
import pl.slaszu.todoapp.domain.TodoModel
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

@Database(entities = [TodoModel::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoModelDao(): TodoModelDao
}