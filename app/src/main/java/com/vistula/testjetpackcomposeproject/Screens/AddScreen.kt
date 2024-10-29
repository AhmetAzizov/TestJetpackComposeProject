package com.vistula.testjetpackcomposeproject.Screens

import android.net.Uri
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
import com.vistula.testjetpackcomposeproject.ItemViewModel
import com.vistula.testjetpackcomposeproject.R
import com.vistula.testjetpackcomposeproject.Utils.Screen
import com.vistula.testjetpackcomposeproject.ui.theme.TestJetpackComposeProjectTheme

@Composable
fun AddScreen(
    viewModel: ItemViewModel,
    navController: NavHostController
) {
    AddScreenContent(
            navController = navController,
            nameTextField = viewModel.nameTextField,
            setNameTextField = { viewModel.nameTextField = it },
            updateImageUri = { viewModel.imageUri = it },
            imageUri = viewModel.imageUri
        )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreenContent(
    navController: NavHostController,
    nameTextField: String,
    setNameTextField: (value: String) -> Unit,
    imageUri: Uri?,
    updateImageUri: (uri: Uri) -> Unit
) {
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
                value = nameTextField,
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
                imageUri?.let { uri ->
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxSize(),
                        model = ImageRequest.Builder(LocalContext.current)
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
            nameTextField = "ksdfsdf",
            setNameTextField = {},
            imageUri = null,
            updateImageUri = {}
        )
    }
}