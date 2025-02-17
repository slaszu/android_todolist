package pl.slaszu.todoapp.infrastructure.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TodoModelEntity::class],
    version = 1,
//    autoMigrations = [
//        AutoMigration(from = 1, to = 2)
//    ]
)
@TypeConverters(LocalDateTimeConverter::class, RepeatTypeConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun todoModelDao(): TodoModelDao
}