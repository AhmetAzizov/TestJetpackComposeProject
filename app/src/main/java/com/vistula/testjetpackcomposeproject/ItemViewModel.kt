package com.vistula.testjetpackcomposeproject

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.modifier.modifierLocalMapOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.vistula.testjetpackcomposeproject.Models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.log

class ItemViewModel: ViewModel() {

    private val TAG = "ItemViewModel"

    private val personCollectionRef = Firebase.firestore.collection("items")
    var itemList by mutableStateOf(listOf<Item>())

    private var _nameTextField by mutableStateOf("")
    var nameTextField: String
        get() = _nameTextField
        set(value) { _nameTextField = value }

    private var _imageUri by mutableStateOf<Uri?>(null)
    var imageUri: Uri?
        get() = _imageUri
        set(value) { _imageUri = value }

    private var _loading by mutableStateOf(false)
    var loading: Boolean
        get() = _loading
        set(value) { _loading = value }


    init {
        subscribeData()
    }

    private fun subscribeData() {
        personCollectionRef.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
            firebaseFirestoreException?.let {
                it.message?.let { it1 -> Log.d(TAG, it1) }
                return@addSnapshotListener
            }

            val items = mutableListOf<Item>()

            querySnapshot?.let {
                for (document in querySnapshot.documents) {
                    document.toObject<Item>()?.let {    // adds item if document isn't null
                        items.add(it)
                    }
                }

                itemList = items
            }
        }
    }

    fun uploadImageToFirebase(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit)
    {
        _loading = true

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/image1.jpg")

        imageRef.putFile(uri)
            .addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    onSuccess(downloadUri.toString()) // Get the download URL

                    _loading = false

                    Log.d(TAG, "uploaded successfully")
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)

                    _loading = false

                Log.d(TAG, "upload failed!")
            }
    }

    override fun onCleared() {
        super.onCleared()

        Log.d(TAG, "onCleared")
    }
}