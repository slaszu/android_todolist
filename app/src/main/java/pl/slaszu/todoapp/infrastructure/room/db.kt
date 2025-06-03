package pl.slaszu.todoapp.infrastructure.room

import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.AutoMigrationSpec
import androidx.sqlite.db.SupportSQLiteDatabase
import java.util.UUID

@Database(
    entities = [TodoModelEntity::class],
    version = 3,
    autoMigrations = [
        AutoMigration(from = 1, to = 2),
        AutoMigration(from = 2, to = 3, spec = AppDatabase.ChangePrimaryKeyIntToUUIDString::class)
    ]
)
@TypeConverters(LocalDateTimeConverter::class, RepeatTypeConverter::class)

abstract class AppDatabase : RoomDatabase() {
    abstract fun todoModelDao(): TodoModelDao

    class ChangePrimaryKeyIntToUUIDString : AutoMigrationSpec {
        @Override
        override fun onPostMigrate(db: SupportSQLiteDatabase) {
            val cursor = db.query("SELECT id FROM todo")
            for(i in 0..< cursor.count) {
                cursor.moveToPosition(i)
                val id = cursor.getString(0)
                val uuid = UUID.randomUUID().toString()
                db.execSQL("UPDATE todo SET id = '$uuid' where id = '$id'")
            }
        }
    }
}
