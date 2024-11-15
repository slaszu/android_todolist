package pl.slaszu.todoapp.domain

import java.time.LocalDateTime

interface TodoModel {
    val id: Int?
    val text: String
    val done: Boolean
    val priority: Int
    val startDate: LocalDateTime?

    fun copy(vararg fields: Pair<String, Any?>): TodoModel
}

interface TodoModelFactory<T:TodoModel> {
    fun createDefault(): T
}

data class TodoModelFake(
    override val id: Int? = null,
    override val text: String = "fake",
    override val done: Boolean = false,
    override val priority: Int = 0,
    override val startDate: LocalDateTime? = null
) : TodoModel {
    override fun copy(vararg fields: Pair<String, Any?>): TodoModel {
        throw NotImplementedError(message = "${this::class}.copy error")
    }
}