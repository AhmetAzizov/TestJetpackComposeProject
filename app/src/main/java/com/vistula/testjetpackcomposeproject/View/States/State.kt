package com.vistula.testjetpackcomposeproject.View.States

import android.net.Uri
import com.vistula.testjetpackcomposeproject.Models.Item

data class State(
    var itemList: List<Item> = emptyList(),
    var nameTextField: String = "",
    var imageUri: Uri? = null,
    var loading: Boolean = false
)
