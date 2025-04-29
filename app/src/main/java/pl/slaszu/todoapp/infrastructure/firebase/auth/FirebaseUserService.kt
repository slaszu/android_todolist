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
import pl.slaszu.todoapp.domain.auth.UserService
import javax.inject.Inject

class FirebaseUserService @Inject constructor(
    @ApplicationContext
    private val context: Context
) : UserService {

    // [START declare_auth]
    // [START initialize_auth]
    // Initialize Firebase Auth
    private var auth: FirebaseAuth = Firebase.auth
    // [END declare_auth]

    // [END initialize_auth]
    // [START declare_credential_manager]
    private var credentialManager: CredentialManager = CredentialManager.create(context)
    // [END declare_credential_manager]

    override fun startLogInProcess(coroutineScope: CoroutineScope) {
        // [START create_credential_manager_request]
        // Instantiate a Google sign-in request
        val googleIdOption = GetGoogleIdOption
            //.Builder("272014256678-j2rkvcvvk3bkro10f6jafld70r3i7rhi.apps.googleusercontent.com")
            // Your server's client ID, not your Android client ID.
            .Builder()
            .setServerClientId("272014256678-j2rkvcvvk3bkro10f6jafld70r3i7rhi.apps.googleusercontent.com")
            //.setFilterByAuthorizedAccounts(true)
            .build()

        // Create the Credential Manager request
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()
        // [END create_credential_manager_request]

        coroutineScope.launch {
            try {
                // Launch Credential Manager UI
                val result = credentialManager.getCredential(
                    context = context,
                    request = request
                )

                // Extract credential from the result returned by Credential Manager
                handleSignIn(result.credential)
            } catch (e: GetCredentialException) {
                Log.e("myapp", "Couldn't retrieve user's credentials: ${e.localizedMessage}")
            }
        }
    }

    override fun startLogoutProcess(coroutineScope: CoroutineScope) {
        coroutineScope.launch {
            auth.signOut()

            try {
                val clearRequest = ClearCredentialStateRequest()
                credentialManager.clearCredentialState(clearRequest)
                //
            } catch (e: ClearCredentialException) {
                Log.e("myapp", "Couldn't clear user credentials: ${e.localizedMessage}")
            }
        }
    }

    // [START handle_sign_in]
    private fun handleSignIn(credential: Credential) {
        Log.d("myapp", credential.toString())
        // Check if credential is of type Google ID
        if (credential is CustomCredential && credential.type == TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
            // Create Google ID Token
            val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
        } else {
            Log.w("myapp", "Credential is not of type Google ID!")
        }
    }
    // [END handle_sign_in]

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("myapp", "signInWithCredential:success")
                    val user = auth.currentUser
                    //    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user
                    Log.w("myapp", "signInWithCredential:failure", task.exception)
                    //    updateUI(null)
                }
            }
    }
}