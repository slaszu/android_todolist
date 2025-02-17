package pl.slaszu.todoapp.domain

import pl.slaszu.todoapp.domain.repeat.RepeatType
import java.time.LocalDateTime

interface TodoModel {
    val id: Long
    val text: String
    val done: Boolean
    val startDate: LocalDateTime?
    val repeatType: RepeatType?

    fun copy(vararg fields: Pair<String, Any?>): TodoModel
}

interface TodoModelFactory<T : TodoModel> {
    fun createDefault(): T
}

data class FakeTodoModel(
    override val id: Long = 0,
    override val text: String = "fake",
    override val done: Boolean = false,
    override val startDate: LocalDateTime? = null,
    override val repeatType: RepeatType? = null
) : TodoModel {
    override fun copy(vararg fields: Pair<String, Any?>): TodoModel {
        throw NotImplementedError(message = "${this::class}.copy error")
    }
}