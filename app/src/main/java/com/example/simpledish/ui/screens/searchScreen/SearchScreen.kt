package com.example.simpledish.ui.screens.searchScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.simpledish.AppViewModelProvider
import com.example.simpledish.R
import com.example.simpledish.SearchBar
import com.example.simpledish.SimpleDishBottomAppBar
import com.example.simpledish.SimpleDishTopAppBar
import com.example.simpledish.data.dummyData.dishList
import com.example.simpledish.model.Dish
import com.example.simpledish.navigation.HomeDestination
import com.example.simpledish.navigation.SearchDestination
import com.example.simpledish.ui.screens.homeScreen.DishList
import com.example.simpledish.ui.screens.loginScreen.LoginScreenViewModel
import com.example.simpledish.ui.theme.SimpleDishTheme

@Composable
fun SearchScreen(
    navigationToDishDetail: (Int, Int) -> Unit, // (it.id, userId)
    onHomeTabClick: (Int) -> Unit,
    onAddTabClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController,
    viewModel: SearchScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {


    viewModel.reinitialize() // re calls data base when nav back to home screen


    val uiState by viewModel.uiState.collectAsState()



    Scaffold(
        topBar = {
            SimpleDishTopAppBar(
                canNavigateBack = false, title = stringResource(
                    SearchDestination.titleRes
                )
            )
        },
        bottomBar = {
            SimpleDishBottomAppBar(
                currScreen = SearchDestination.route,
                onSearchTabClick = {},
                onHomeTabClick = { onHomeTabClick(uiState.userId) },
                onAddTabClick = { onAddTabClick(uiState.userId) })
        },
        snackbarHost = {},
        modifier = modifier
    ) { padding ->

        SearchScreenBody(
            uiState = uiState,
            onDishClick = navigationToDishDetail,
            onSearchValueChange = viewModel::onSearch,
            onSearchByIngredient = viewModel::onIngredientButton,
            onSearchByName = viewModel::onNameButton,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun SearchScreenBody(
    uiState: SearchScreenUIState,
    onSearchByName: () -> Unit,
    onSearchByIngredient: () -> Unit,
    onDishClick: (Int, Int) -> Unit,
    onSearchValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {

        SearchBarAll(
            searchValue = uiState.search,
            label = "Search dishes",
            onSearchValueChange = onSearchValueChange,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.size(6.dp))

        Row {
            Button(
                onClick = onSearchByName,
                enabled = uiState.search.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Search by name")
            }
            Spacer(modifier = Modifier.size(6.dp))
            Button(
                onClick = onSearchByIngredient,
                enabled = uiState.search.isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text("Search by ingredient")
            }
        }

        Spacer(modifier = Modifier.size(14.dp))

        if (uiState.listOfDishesSearch.isEmpty() && uiState.searched) {
            Text("No matches.")
        } else {

            if (!uiState.searched) {
                Text("All dishes")

                DishList(
                    dishList = uiState.listOfDishes,
                    onDishClick = onDishClick,
                    userId = uiState.userId,
                    modifier = Modifier.padding(10.dp)
                )
            } else {
                DishList(
                    dishList = uiState.listOfDishesSearch,
                    onDishClick = onDishClick,
                    userId = uiState.userId,
                    modifier = Modifier.padding(10.dp)
                )
            }
        }
    }
}

@Composable
fun SearchBarAll(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = searchValue,
        leadingIcon = { Icon(painter = painterResource(id = R.drawable.search_icon), null) },
        onValueChange = onSearchValueChange,
        singleLine = true,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        modifier = modifier
    )
}

@Preview(showSystemUi = true)
@Composable
fun SearchScreenPreview() {
    SimpleDishTheme {
        SearchScreenBody(
            onDishClick = { _, _ -> },
            onSearchValueChange = {},
            onSearchByIngredient = {},
            onSearchByName = {},
            uiState = SearchScreenUIState(userId = 0)
        )
    }
}