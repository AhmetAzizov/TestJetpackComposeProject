package com.vistula.testjetpackcomposeproject.Screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vistula.testjetpackcomposeproject.ItemViewModel
import com.vistula.testjetpackcomposeproject.Models.Item
import com.vistula.testjetpackcomposeproject.ui.theme.TestJetpackComposeProjectTheme



@Composable
fun MainScreen(
    viewModel: ItemViewModel
) {
    val itemList = viewModel.itemList

    MainScreenContent(
        itemList
    )
}

@Composable
fun MainScreenContent(
    itemList: List<Item>
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = Color.Gray
    ){
        itemsList(itemList = itemList)
    }
}


@Composable
fun itemsList(
    modifier: Modifier = Modifier,
    itemList: List<Item>
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(
            itemList,
            key = { it.name }
        ) {
            itemCard(item = it)
        }
    }
}


@Composable
fun itemCard(
    modifier: Modifier = Modifier,
    item: Item
) {
    Card(
        modifier = modifier
            .padding(10.dp)
    ) {
        Column {
            Text(
                modifier = Modifier
                    .padding(5.dp),
                text = item.name
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TestJetpackComposeProjectTheme {
        MainScreenContent(
            itemList = listOf(
                Item(name = "ksdfsd"),
                Item(name = "asdfksdf"),
                Item(name = "q1qweqwea"),
                Item(name = "asdfwerwer"),
            )
        )
    }
}