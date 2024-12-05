package pl.slaszu.todoapp.infrastructure.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.slaszu.todoapp.domain.TodoModel
import pl.slaszu.todoapp.domain.TodoModelFactory
import java.time.LocalDateTime
import javax.inject.Inject

@Entity(tableName = "todo")
data class TodoModelEntity(
    @PrimaryKey(autoGenerate = true) override val id: Long = 0,
    @ColumnInfo(name = "text") override val text: String = "",
    @ColumnInfo(name = "done") override val done: Boolean = false,
    @ColumnInfo(name = "priority") override val priority: Int = 0,
    @ColumnInfo(name = "start_date") override val startDate: LocalDateTime? = null
) : TodoModel {
    override fun copy(vararg fields: Pair<String, Any?>): TodoModel {
        val fieldMap = fields.toMap()
        return this.copy(
            text = fieldMap.getOrDefault("text", this.text) as String,
            done = fieldMap.getOrDefault("done", this.done) as Boolean,
            priority = fieldMap.getOrDefault("priority", this.priority) as Int,
            startDate = fieldMap.getOrDefault("startDate", this.startDate) as LocalDateTime?
        )
    }
}

class TodoModelEntityFactory @Inject constructor() : TodoModelFactory<TodoModelEntity> {
    override fun createDefault(): TodoModelEntity {
        return TodoModelEntity()
    }
}