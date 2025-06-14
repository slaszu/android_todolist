package pl.slaszu.todoapp.infrastructure.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import pl.slaszu.todoapp.domain.repeat.RepeatType
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.domain.todo.TodoModelFactory
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject

@Entity(
    tableName = "todo",
    indices = [Index(value = ["text"])]
)
data class TodoModelEntity(
    @PrimaryKey override val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "text") override val text: String = "",
    @ColumnInfo(name = "done") override val done: Boolean = false,
    @ColumnInfo(name = "start_date") override val startDate: LocalDateTime? = null,
    @ColumnInfo(name = "repeat_type") override val repeatType: RepeatType? = null
) : TodoModel {
    override fun copy(vararg fields: Pair<String, Any?>): TodoModel {
        val fieldMap = fields.toMap()
        return this.copy(
            text = fieldMap.getOrDefault("text", this.text) as String,
            done = fieldMap.getOrDefault("done", this.done) as Boolean,
            startDate = fieldMap.getOrDefault("startDate", this.startDate) as LocalDateTime?,
            repeatType = fieldMap.getOrDefault("repeatType", this.repeatType) as RepeatType?,
        )
    }
}

class TodoModelEntityFactory @Inject constructor() : TodoModelFactory<TodoModelEntity> {
    override fun createDefault(): TodoModelEntity {
        return TodoModelEntity()
    }
}