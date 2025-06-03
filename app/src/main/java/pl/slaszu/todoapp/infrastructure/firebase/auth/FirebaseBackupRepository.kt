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
            .document(item.id)
            .set(item)
            .addOnCompleteListener {
                Log.d("myapp", "Firestore. Document set ok")
            }
            .addOnFailureListener {
                Log.e("myapp", "Firestore. Save item error: ${it.message.toString()}")
            }
    }

    override fun delItem(item: TodoModel, user: User) {
        db.collection("${user.id}")
            .document(item.id)
            .delete()
            .addOnCompleteListener {
                Log.d("myapp", "Firestore. Document deleted ok")
            }
            .addOnFailureListener {
                Log.e("myapp", "Firestore. Delete item error: ${it.message.toString()}")
            }
    }

    override fun getAll(user: User): Array<TodoModel> {
        db.collection("${user.id}")
            .get()
            .addOnSuccessListener { result ->
                Log.d("myapp","Fetched ${result.size()} documents")
                for (document in result) {
                    Log.d("myapp", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.d("myapp", "Firestore. Get all Error: ", exception)
            }

        return arrayOf()
    }

    override fun exportAll(items: Array<TodoModel>, user: User) {
        TODO("Not yet implemented")
    }
}