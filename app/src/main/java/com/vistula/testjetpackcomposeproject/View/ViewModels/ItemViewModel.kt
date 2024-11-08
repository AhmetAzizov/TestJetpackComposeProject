package com.vistula.testjetpackcomposeproject.View.ViewModels

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.vistula.testjetpackcomposeproject.Models.Item
import com.vistula.testjetpackcomposeproject.View.States.State
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

private const val TAG = "ItemViewModel"

class ItemViewModel: ViewModel() {

    private val personCollectionRef = Firebase.firestore.collection("items")

    var sampleState by mutableStateOf(State())
        private set

    fun updateName(newName: String) {
        sampleState = sampleState.copy(nameTextField = newName)
    }

    fun updateImage(uri: Uri) {
        sampleState = sampleState.copy(imageUri = uri)
    }

    private fun toggleLoading(value: Boolean) {
        sampleState = sampleState.copy(loading = value)
    }

    fun addItem(item: Item) {
        val updatedList = (sampleState.itemList ?: emptyList()) + item
        sampleState = sampleState.copy(itemList = updatedList)
    }


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

                sampleState = sampleState.copy(itemList = items)
            }
        }
    }

    fun uploadImageToFirebase(
        uri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit)
    {
        toggleLoading(true)

        val storageRef = FirebaseStorage.getInstance().reference
        val imageRef = storageRef.child("images/image1.jpg")

//        imageRef.putFile(uri)
//            .addOnSuccessListener {
//                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
//                    onSuccess(downloadUri.toString()) // Get the download URL
//
//                    toggleLoading(false)
//
//                    Log.d(TAG, "uploaded successfully")
//                }
//            }
//            .addOnFailureListener { exception ->
//                onFailure(exception)
//
//                toggleLoading(false)
//
//                Log.d(TAG, "upload failed!")
//            }

        viewModelScope.launch {
            try {
                imageRef.putFile(uri).await()
                val downloadUrl = imageRef.downloadUrl.await()

                withContext(Dispatchers.Main) {
                    onSuccess(downloadUrl.toString())
                }

                toggleLoading(false)
                Log.d(TAG, "uploaded successfully")
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onFailure(e)
                }

                toggleLoading(false)
                Log.d(TAG, "upload failed!")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()

        Log.d(TAG, "onCleared")
    }
}