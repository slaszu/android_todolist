package pl.slaszu.todoapp.domain.todo

import pl.slaszu.todoapp.domain.repeat.RepeatType
import java.time.LocalDateTime
import java.util.UUID

interface TodoModel {
    val id: String
    val text: String
    val done: Boolean
    val startDate: LocalDateTime?
    val repeatType: RepeatType?

    fun copy(vararg fields: Pair<String, Any?>): TodoModel

    fun finishCopy(): TodoModel? {

        if (done) return null

        if (startDate != null && repeatType != null) {
            return copy(
                "startDate" to repeatType!!.calculateNewDate(startDate!!)
            )
        }

        return copy(
            "done" to true
        )
    }

}

interface TodoModelFactory<T : TodoModel> {
    fun createDefault(): T
}

data class FakeTodoModel(
    override val id: String = UUID.randomUUID().toString(),
    override val text: String = "fake",
    override val done: Boolean = false,
    override val startDate: LocalDateTime? = null,
    override val repeatType: RepeatType? = null
) : TodoModel {
    override fun copy(vararg fields: Pair<String, Any?>): TodoModel {
        throw NotImplementedError(message = "${this::class}.copy error")
    }
}