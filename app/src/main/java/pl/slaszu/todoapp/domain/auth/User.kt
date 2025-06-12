package pl.slaszu.todoapp.domain.auth

import android.os.Parcelable
import kotlinx.coroutines.CoroutineScope
import kotlinx.parcelize.Parcelize


@Parcelize
data class User(val id: String, val email: String) : Parcelable

interface UserService {
    fun getUserOrNull(): User?
}

interface UserActivityService {
    fun startLogInProcess(coroutineScope: CoroutineScope, callback: (User) -> Unit)
    fun startLogoutProcess(coroutineScope: CoroutineScope, callback: () -> Unit)
}