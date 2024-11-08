package com.vistula.testjetpackcomposeproject.View.ViewModels

import android.net.Uri
import android.util.Log
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.vistula.testjetpackcomposeproject.Models.RegistrationUser
import com.vistula.testjetpackcomposeproject.View.States.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val TAG = "AuthenticationViewModel"

class AuthenticationViewModel: ViewModel() {
    private var auth = FirebaseAuth.getInstance()
    private val usersCollectionRef = Firebase.firestore.collection("users")
    private val storageRef = FirebaseStorage.getInstance().reference

    var authState by mutableStateOf(AuthState())
        private set

    private fun updateLoginStatus(value: Boolean) {
        authState = authState.copy(isLoggedIn = value)
    }

    fun updateUsernameEmailTextField(value: String) {
        authState = authState.copy(usernameTextField = value)
    }

    fun updateEmailTextField(value: String) {
        authState = authState.copy(emailTextField = value)
    }

    fun updateImageUri(uri: Uri) {
        authState = authState.copy(imageUri = uri)
    }

    fun updatePasswordTextField(value: String) {
        authState = authState.copy(passwordTextField = value)
    }

    fun toggleRegisterLoading(value: Boolean) {
        authState = authState.copy(registerLoading = value)
    }

    init {
        checkLoggedInState()
    }

    fun registerUser(
        onSuccess: () -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val imageError = if (authState.imageUri == null) "Image is not selected"
        else null

        val usernameError = if (authState.usernameTextField.isEmpty()) "Username cannot be empty"
        else if (containsSpecialCharacter(authState.usernameTextField)) "Username cannot contain special characters"
        else null

        val emailError = if (authState.emailTextField.isEmpty()) "Email cannot be empty"
        else if (!isValidEmail(authState.emailTextField)) "Please enter a valid email"
        else null

        val passwordError = if (authState.passwordTextField.isEmpty()) "Password cannot be empty!"
        else if (authState.passwordTextField.length < 6) "Password length should not be less then 6 characters!"
        else null

        authState = authState.copy(
            imageError = imageError,
            usernameError = usernameError,
            emailError = emailError,
            passwordError = passwordError
        )

        if (imageError != null || usernameError != null || emailError != null || passwordError != null) return

        toggleRegisterLoading(true)

        viewModelScope.launch {
            try {
                auth.createUserWithEmailAndPassword(authState.emailTextField, authState.passwordTextField).await()
                val currentUser = auth.currentUser ?: throw Exception("currentUser is null")

                updateUser(currentUser, authState.imageUri!!, authState.usernameTextField)
                val imageUrl = uploadUserImage(authState.imageUri!!, authState.usernameTextField)
                uploadUserData(currentUser, imageUrl)

                withContext(Dispatchers.Main) {
                    onSuccess()
                }

                toggleRegisterLoading(false)
                checkLoggedInState()
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }
                Log.e(TAG, "registerUser: ${e.message}")
                toggleRegisterLoading(false)
            }
        }
    }

    private suspend fun uploadUserImage(imageUri: Uri, username: String): String {
        val imageRef = storageRef.child("images/$username.jpg")

        imageRef.putFile(imageUri).await()
        Log.d(TAG, "uploadUserImage: Successfully uploaded user profile image")
        return imageRef.downloadUrl.await().toString()
    }

    private suspend fun updateUser(user: FirebaseUser, photoUri: Uri, username: String) {
        val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .setPhotoUri(photoUri)
            .build()

        user.updateProfile(profileUpdates).await()
        Log.d(TAG, "updateUser: Successfully updated user profile")
    }

    private suspend fun uploadUserData(user: FirebaseUser, imageUrl: String){
        val newUser = RegistrationUser(
            uid = user.uid,
            imageUrl = imageUrl,
            userName = user.displayName!!,
            email = user.email!!
        )

        usersCollectionRef.document(user.displayName!!).set(newUser).await()
        Log.d(TAG, "uploadUserData: Successfully uploaded user data")
    }

    private fun checkLoggedInState() {
        if (auth.currentUser == null) {
            updateLoginStatus(false)
        } else {
            updateLoginStatus(true)
        }
    }

    private fun containsSpecialCharacter(input: String): Boolean {
        val regex = "[^a-zA-Z0-9]".toRegex()
        return regex.containsMatchIn(input)
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}