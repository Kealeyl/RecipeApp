package com.example.simpledish.ui.screens.addDishScreen

import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpledish.AppViewModelProvider
import com.example.simpledish.SimpleDishBottomAppBar
import com.example.simpledish.SimpleDishTopAppBar
import com.example.simpledish.data.dummyData.dish2
import com.example.simpledish.data.dummyData.ingredientList
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.navigation.AddDestination
import com.example.simpledish.navigation.SearchDestination

@Composable
fun AddScreen(
    modifier: Modifier = Modifier,
    onSearchTabClick: (Int) -> Unit,
    onHomeTabClick: (Int) -> Unit,
    viewModel: AddDishScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SimpleDishTopAppBar(
                canNavigateBack = false, title = stringResource(
                    AddDestination.titleRes
                )
            )
        },
        bottomBar = {
            SimpleDishBottomAppBar(
                currScreen = AddDestination.route,
                onHomeTabClick = { onHomeTabClick(uiState.userId) },
                onAddTabClick = {},
                onSearchTabClick = { onSearchTabClick(uiState.userId) })
        },
        snackbarHost = {},
        modifier = modifier
    ) { padding ->

        AddScreenBody(
            dish = uiState.dish,
            ingredients = uiState.ingredients,
            onValueChange = viewModel::updateUiState,
            onValueChangeIngredient = viewModel::updateIngredientsState,
            isValidEntry = uiState.isEntryValid,
            onButton = {
                // submit to DB and wipe the text fields
                viewModel.addToDatabase()
                viewModel.resetDishAndIngredients()
            },
            isAddScreen = true,
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun AddScreenBody(
    dish: Dish,
    ingredients: List<Ingredient>,
    onValueChange: (Dish) -> Unit,
    onValueChangeIngredient: (Ingredient, Int) -> Unit,
    onButton: () -> Unit,
    isAddScreen: Boolean,
    isValidEntry: Boolean,
    modifier: Modifier = Modifier
) {

    val context = LocalContext.current // for the toast

    Column(modifier = modifier.padding(8.dp)) {
        OutlinedTextField(
            value = dish.name,
            onValueChange = { onValueChange(dish.copy(name = it)) },
            singleLine = true,
            label = { Text("Dish Name*") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.size(16.dp))

        Row {
            OutlinedTextField(
                value = dish.prepTime.hour,
                onValueChange = {
                    onValueChange(
                        dish.copy(
                            prepTime = dish.prepTime.copy(hour = it)
                        )
                    )
                },
                singleLine = true,
                label = { Text("Hour time*") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))

            OutlinedTextField(
                value = dish.prepTime.minute,
                onValueChange = {
                    onValueChange(
                        dish.copy(
                            prepTime = dish.prepTime.copy(minute = it)
                        )
                    )
                },
                singleLine = true,
                label = { Text("Min time*") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.size(8.dp))

            OutlinedTextField(
                value = dish.servings,
                onValueChange = {
                    onValueChange(dish.copy(servings = it))
                },
                singleLine = true,
                label = { Text("Servings") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1f)
            )
        }

        Text(
            text = "Ingredients",
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            textAlign = TextAlign.Center,
            fontSize = 16.sp
        )

        enterDishIngredients(
            ingredients,
            onValueChange = onValueChangeIngredient,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        Spacer(modifier = Modifier.size(16.dp))

        if (!isValidEntry) {
            Text("Missing *required fields")
            Spacer(modifier = Modifier.size(16.dp))
        }

        Button(onClick = {
            val text = if (isAddScreen) "Added" else "Edited"
            onButton()
            Toast.makeText(context, "$text ${dish.name}", Toast.LENGTH_SHORT).show()

        }, enabled = isValidEntry) {

            val text = if (isAddScreen) "Add" else "Edit"

            Text(text, modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun enterDishIngredients(
    ingredients: List<Ingredient>,
    onValueChange: (Ingredient, Int) -> Unit,
    modifier: Modifier = Modifier
) {

    for (i in ingredients.indices) {

        val finalAction = if (i == ingredients.size - 1) ImeAction.Done else ImeAction.Next

        Row(modifier = modifier) {
            OutlinedTextField(
                value = ingredients[i].ingredientName,
                onValueChange = {
                    onValueChange(
                        ingredients[i].copy(ingredientName = it),
                        i
                    )
                },
                singleLine = true,
                label = { if (i == 0) Text("Name*") else Text("Name") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(3f)
            )

            Spacer(modifier = Modifier.size(8.dp))

            OutlinedTextField(
                value = ingredients[i].quantity,
                onValueChange = {
                    onValueChange(
                        ingredients[i].copy(quantity = it), i
                    )
                },
                singleLine = true,
                label = { if (i == 0) Text("Quant*") else Text("Quant") },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                modifier = Modifier.weight(1.2f)
            )

            Spacer(modifier = Modifier.size(8.dp))

            OutlinedTextField(
                value = ingredients[i].measurementUnit,
                onValueChange = {
                    onValueChange(
                        ingredients[i].copy(measurementUnit = it),
                        i
                    )
                },
                singleLine = true,
                label = { if (i == 0) Text("Unit*") else Text("Unit") },

                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = finalAction
                ),
                modifier = Modifier.weight(1f)
            )
        }
    }
}


@Preview(showSystemUi = true)
@Composable
fun AddScreenBodyPreview() {
    AddScreenBody(
        dish = dish2,
        isValidEntry = true,
        onValueChange = {},
        onValueChangeIngredient = { _, _ -> },
        isAddScreen = true,
        ingredients = emptyList(),
        onButton = {})
}

@Preview(showSystemUi = true)
@Composable
fun IngredientsPreview() {
    Column {
        enterDishIngredients(
            ingredientList,
            onValueChange = { _, _ -> },
            modifier = Modifier.padding(bottom = 4.dp)
        )
    }


}
