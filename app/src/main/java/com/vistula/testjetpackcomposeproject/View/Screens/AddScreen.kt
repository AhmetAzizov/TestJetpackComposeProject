package com.vistula.testjetpackcomposeproject.View.Screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.vistula.testjetpackcomposeproject.View.ViewModels.ItemViewModel
import com.vistula.testjetpackcomposeproject.R
import com.vistula.testjetpackcomposeproject.Utils.Screen
import com.vistula.testjetpackcomposeproject.View.States.State
import com.vistula.testjetpackcomposeproject.ui.theme.TestJetpackComposeProjectTheme

private const val TAG = "AddScreen"

@Composable
fun AddScreen(
    viewModel: ItemViewModel,
    navController: NavHostController
) {
    AddScreenContent(
            navController = navController,
            state = viewModel.sampleState,
            setNameTextField = { viewModel.updateName(it) },
            updateImageUri = { viewModel.updateImage(it) },
            uploadImage = { uri, onSuccess, onFailure ->
                viewModel.uploadImageToFirebase(uri = uri, onSuccess, onFailure)
            }
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreenContent(
    navController: NavHostController,
    state: State,
    setNameTextField: (value: String) -> Unit,
    updateImageUri: (uri: Uri) -> Unit,
    uploadImage: (uri: Uri, (String) -> Unit, (Exception) -> Unit) -> Unit,
) {
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.LightGray),
                title = {
                        Text(text = "Add Item")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigate(Screen.MainScreen.route)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back Button")
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .padding(16.dp),
                placeholder = { Text("Name") },
                value = state.nameTextField,
                onValueChange = {value ->
                    setNameTextField(value)
                }
            )

            OutlinedButton(
                modifier = Modifier
                    .padding(horizontal = 16.dp),
                onClick = { /*TODO*/ }
            ) {
                Text("Add")
            }


            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.GetContent()
            ) { uri: Uri? ->
                uri?.let { updateImageUri(uri) }
            }

            Card(
                onClick = {
                    launcher.launch("image/*")
                },
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .width(200.dp)
                    .height(200.dp)
                    .align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(100.dp),
                border = BorderStroke(2.dp, Color.White)
            ) {
                state.imageUri?.let { uri ->
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = ImageRequest.Builder(context)
                            .data(uri)
                            .crossfade(400).
                            build(),
                        contentDescription = "image",
                        contentScale = ContentScale.Crop
                    )
                }.let {
                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Icon(
                            modifier = Modifier
                                .align(Alignment.Center)
                                .size(32.dp),
                            painter = painterResource(R.drawable.image_search),
                            contentDescription = "photo icon"
                        )
                    }
                }
            }

            Button(
                onClick = {
                    state.imageUri?.let {
                        uploadImage(
                            state.imageUri!!,
                            { downloadUrl ->
                                Toast.makeText(context, "Image uploaded successfully!", Toast.LENGTH_SHORT).show()
                                Log.d(TAG, "download Url: $downloadUrl")
                            },
                            {exception ->
                                Toast.makeText(context, exception.message, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }

                },
                modifier = Modifier
                    .padding(16.dp),
                enabled = state.imageUri != null && !state.loading
            ) {
                Text(text = "Upload Image")
            }

            if (state.loading) CircularProgressIndicator()

            Button(onClick = {
                navController.navigate("auth") {
                    popUpTo("main") {
                        inclusive = true
                    }
                }
            }) {
                Text(text = "Authentication Screen")
            }
        }
    }
}

@Composable
fun ImagePicker(
    onImagePicked: (Uri) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { onImagePicked(it) }
    }

    Button(onClick = { launcher.launch("image/*") }) {
        Text("Select Image")
    }
}

@Preview(showBackground = true)
@Composable
fun AddScreenPreview() {
    TestJetpackComposeProjectTheme {
        AddScreenContent(
            navController = rememberNavController(),
            state = State(),
            setNameTextField = {},
            updateImageUri = {},
            uploadImage = { uri, onSuccess, onFailure -> },
        )
    }
}