package pl.slaszu.todoapp.domain.navigation

import kotlinx.serialization.Serializable

@Serializable
object TodoAppRouteList

@Serializable
object TodoAppRouteEditOrNewForm

@Serializable
object TodoAppSetting

@Serializable
data class TodoAppReminderItems(val ids: LongArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TodoAppReminderItems

        return ids.contentEquals(other.ids)
    }

    override fun hashCode(): Int {
        return ids.contentHashCode()
    }
}



