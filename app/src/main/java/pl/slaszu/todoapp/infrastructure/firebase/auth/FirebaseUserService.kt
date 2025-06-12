package pl.slaszu.todoapp.infrastructure.firebase.auth

import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.auth.UserService
import javax.inject.Inject

class FirebaseUserService @Inject constructor() : UserService {

    private var auth: FirebaseAuth = Firebase.auth

    override fun getUserOrNull(): User? {
        return this.auth.currentUser?.let {
            User(it.uid, it.email.orEmpty())
        }
    }
}