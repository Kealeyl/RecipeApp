package com.example.simpledish.ui.screens.editDishScreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpledish.AppViewModelProvider
import com.example.simpledish.SimpleDishTopAppBar
import com.example.simpledish.data.dummyData.dish2
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.navigation.DetailsDestination
import com.example.simpledish.navigation.EditDishDestination
import com.example.simpledish.ui.screens.addDishScreen.AddScreenBody
import com.example.simpledish.ui.screens.homeScreen.HomeScreenBody

@Composable
fun EditScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: EditDishScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SimpleDishTopAppBar(
                navigateBack = navigateBack,
                canNavigateBack = true,
                title = stringResource(EditDishDestination.titleRes)
            )
        },
        modifier = modifier
    ) { padding ->

        AddScreenBody(
            dish = uiState.dish,
            ingredients = uiState.ingredients,
            onValueChange = viewModel::updateUiState,
            onValueChangeIngredient = viewModel::updateIngredientsState,
            isValidEntry = uiState.isEntryValid,
            onButton = viewModel::updateDatabase,
            isAddScreen = false,
            modifier = Modifier.padding(padding)
        )
    }
}