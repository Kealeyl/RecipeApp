package com.example.simpledish.ui.screens.homeScreen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.simpledish.AppViewModelProvider
import com.example.simpledish.SearchBar
import com.example.simpledish.SimpleDishBottomAppBar
import com.example.simpledish.SimpleDishTopAppBar
import com.example.simpledish.SimpleDishTopAppBarHome
import com.example.simpledish.data.dummyData.dishList
import com.example.simpledish.model.Dish
import com.example.simpledish.navigation.HomeDestination
import com.example.simpledish.navigation.LoginDestination
import com.example.simpledish.ui.screens.editDishScreen.EditDishScreenViewModel
import com.example.simpledish.ui.theme.SimpleDishTheme

@Composable
fun HomeScreen(
    navigationToDishDetail: (Int, Int) -> Unit,
    logOutButton: () -> Unit,
    onAddTabClick: (Int) -> Unit,
    onSearchTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    viewModel.reinitialize() // re calls data base

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SimpleDishTopAppBarHome(
                logOut = logOutButton,
                title = stringResource(HomeDestination.titleRes)
            )
        },
        bottomBar = {
            SimpleDishBottomAppBar(
                currScreen = HomeDestination.route,
                onAddTabClick = { onAddTabClick(uiState.userId) }, // ok
                onHomeTabClick = {},
                onSearchTabClick = { onSearchTabClick(uiState.userId) })
        },
        modifier = modifier
    ) { padding ->

        HomeScreenBody(
            userAddedDishList = uiState.userAddedDishList,
            userSavedDishList = uiState.userSavedDishList,
            onDishClick = navigationToDishDetail,
            userId = uiState.userId,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun HomeScreenBody(
    userAddedDishList: List<Dish>,
    userSavedDishList: List<Dish>,
    onDishClick: (Int, Int) -> Unit,
    userId: Int,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        Spacer(modifier = Modifier.size(10.dp))
        Text("Your contributed dishes", fontSize = 18.sp)

        Spacer(modifier = Modifier.size(8.dp))

        if (userAddedDishList.isEmpty()) {
            Text("No added dishes. Tap + to add a dish")
            Spacer(modifier = Modifier.size(282.dp))
        } else {
            DishList(
                userAddedDishList,
                userId,
                onDishClick,
                modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .height(282.dp)
            )
        }

        Text("Your saved dishes", fontSize = 18.sp)
        Spacer(modifier = Modifier.size(8.dp))

        if (userSavedDishList.isEmpty()) {
            Text(
                "No saved dishes. Search and save a dish",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        } else {
            DishList(
                userSavedDishList, userId, onDishClick, modifier = Modifier
                    .padding(start = 10.dp, end = 10.dp)
                    .height(262.dp)
            )
        }

    }
}

// top app bar -> logout button

// lazy column of user dishes
// empty message if none

@Composable
fun DishList(
    dishList: List<Dish>,
    userId: Int,
    onDishClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(items = dishList, key = { it.id }) {
            DishItem(it, modifier = Modifier
                .padding(bottom = 10.dp)
                .clickable {
                    onDishClick(it.id, userId)
                })
        }
    }
}

@Composable
fun DishItem(dish: Dish, modifier: Modifier = Modifier) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(dish.name, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.size(6.dp))
            Row {
                Text("PrepTime: ${dish.prepTime}", fontSize = 15.sp)
                Spacer(
                    modifier = Modifier
                        .size(16.dp)
                        .weight(1f)
                )
                Text("Servings: ${dish.servings}", fontSize = 15.sp)
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    SimpleDishTheme {
        HomeScreenBody(
            userAddedDishList = dishList,
            onDishClick = { _, _ -> },
            userId = 0,
            userSavedDishList = dishList
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomeScreenEmptyPreview() {
    SimpleDishTheme {
        HomeScreenBody(
            userAddedDishList = emptyList(),
            onDishClick = { _, _ -> },
            userId = 0,
            userSavedDishList = dishList
        )
    }
}