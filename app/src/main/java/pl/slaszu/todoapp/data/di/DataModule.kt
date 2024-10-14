package pl.slaszu.todoapp.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.slaszu.todoapp.data.TodoSimpleRepository
import pl.slaszu.todoapp.domain.TodoRepository

@InstallIn(SingletonComponent::class)
@Module
abstract class DataModule {
    @Binds
    abstract fun getTodoRepository(repo: TodoSimpleRepository): TodoRepository
}