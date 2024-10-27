package pl.slaszu.todoapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface TodoAppRoute

@Serializable
data object TodoAppRouteList: TodoAppRoute

@Serializable
data class TodoAppRouteEditOrNewForm(val todoId: Int? = null): TodoAppRoute

