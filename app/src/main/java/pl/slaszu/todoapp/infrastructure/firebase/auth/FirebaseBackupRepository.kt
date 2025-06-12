package pl.slaszu.todoapp.infrastructure.firebase.auth

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.backup.BackupRepository
import pl.slaszu.todoapp.domain.repeat.RepeatType
import pl.slaszu.todoapp.domain.todo.TodoModel
import pl.slaszu.todoapp.infrastructure.room.LocalDateTimeConverter
import pl.slaszu.todoapp.infrastructure.room.TodoModelEntity

class FirebaseBackupRepository(
    val db: FirebaseFirestore
) : BackupRepository {
    override fun saveItem(item: TodoModel, user: User) {
        db.collection("${user.id}")
            .document(item.id)
            .set(toFirebaseTodoModel(item))
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

    override suspend fun getAll(user: User): Array<TodoModel> =
        withContext(Dispatchers.IO) {
            return@withContext db.collection("${user.id}")
                .get()
                .await()
                .toObjects(FirebaseTodoModel::class.java)
                .map {
                    toTodoModel(it)
                }
                .toTypedArray()
        }

    companion object Converter {
        fun toFirebaseTodoModel(item: TodoModel): FirebaseTodoModel {
            return FirebaseTodoModel(
                id = item.id,
                text = item.text,
                done = item.done,
                startDate = LocalDateTimeConverter().localDateTimeToString(item.startDate),
                repeatType = item.repeatType?.toStringRepresentation()
            )
        }

        fun toTodoModel(item: FirebaseTodoModel): TodoModel {
            return TodoModelEntity(
                id = item.id,
                text = item.text,
                done = item.done,
                startDate = LocalDateTimeConverter().stringToLocalDateTime(item.startDate),
                repeatType = item.repeatType?.let {
                    RepeatType.toObject(it)
                }
            )
        }
    }
}

data class FirebaseTodoModel(
    val id: String = "",
    val text: String = "",
    val done: Boolean = false,
    val startDate: String? = null,
    val repeatType: String? = null
) {
    constructor() : this("")
}

