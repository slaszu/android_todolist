package pl.slaszu.todoapp.infrastructure.firebase.auth

import android.content.Context
import android.util.Log
import androidx.credentials.ClearCredentialStateRequest
import androidx.credentials.Credential
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.ClearCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.Companion.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.slaszu.todoapp.domain.auth.User
import pl.slaszu.todoapp.domain.auth.UserService
import javax.inject.Inject

class FirebaseUserService @Inject constructor(
    @ApplicationContext
    private val context: Context
) : UserService {

    private var auth: FirebaseAuth = Firebase.auth

    private var credentialManager: CredentialManager = CredentialManager.create(context)

    override fun startLogInProcess(coroutineScope: CoroutineScope, callback: (User) -> Unit) {
        val googleIdOption = GetGoogleIdOption
            .Builder()
            .setServerClientId("272014256678-j2rkvcvvk3bkro10f6jafld70r3i7rhi.apps.googleusercontent.com")
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        coroutineScope.launch {
            try {
                // Launch Credential Manager UI
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                // Extract credential from the result returned by Credential Manager
                handleSignIn(result.credential, callback)
            } catch (e: GetCredentialException) {
                Log.e("myapp", "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    override fun startLogoutProcess(coroutineScope: CoroutineScope, callback: () -> Unit) {
        coroutineScope.launch {
            auth.signOut()

            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
                callback()
            } catch (e: ClearCredentialException) {
                Log.e("myapp", "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }
    }

    override fun getUserOrNull(): User? {
        return this.auth.currentUser?.let {
            User(it.uid, it.email.orEmpty())
        }
    }

    private fun handleSignIn(credential: Credential, callback: (User) -> Unit) {
        Log.d("myapp", credential.toString())
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken, callback)
        } else {
            Log.w("myapp", "Credential is not of type Google ID!")
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, callback: (User) -> Unit) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("myapp", "signInWithCredential:success")
                    callback(this.getUserOrNull()!!)
                } else {
                    // If sign in fails, display a message to the user
                    Log.w("myapp", "signInWithCredential:failure", task.exception)
                }
            }
    }
}