package pl.slaszu.todoapp.infrastructure.firebase.auth

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.backup.BackupRepository
import pl.slaszu.todoapp.domain.todo.TodoModel

class FirebaseBackupRepository(
    val db: FirebaseFirestore
) : BackupRepository {
    override fun saveItem(item: TodoModel, user: User) {
        db.collection("${user.id}")
            .document(item.id.toString())
            .set(item)
            .addOnCompleteListener {
                Log.d("myapp", "Firestore. Document set ok")
            }
            .addOnFailureListener {
                Log.e("myapp", "Firestore. Error: ${it.message.toString()}")
            }
    }

    override fun delItem(item: TodoModel, user: User) {
        db.collection("${user.id}")
            .document(item.id.toString())
            .delete()
            .addOnCompleteListener {
                Log.d("myapp", "Firestore. Document deleted ok")
            }
            .addOnFailureListener {
                Log.e("myapp", "Firestore. Error: ${it.message.toString()}")
            }
    }

    override fun getItem(id: Long, user: User) {
        TODO("Not yet implemented")
    }

    override fun recreate(items: Array<TodoModel>, user: User) {
        TODO("Not yet implemented")
    }

}