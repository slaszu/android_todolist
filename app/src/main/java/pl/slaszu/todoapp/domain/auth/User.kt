package pl.slaszu.todoapp.domain.auth

import kotlinx.coroutines.CoroutineScope

data class User(val id: String, val email: String)

interface UserService {
    fun startLogInProcess(coroutineScope: CoroutineScope, callback: (User) -> Unit)
    fun startLogoutProcess(coroutineScope: CoroutineScope, callback: () -> Unit)
    fun getUserOrNull(): User?
}