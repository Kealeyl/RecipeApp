package com.example.simpledish.ui.screens.detailsScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.simpledish.AppViewModelProvider
import com.example.simpledish.SimpleDishTopAppBar
import com.example.simpledish.data.dummyData.dish
import com.example.simpledish.data.dummyData.dish2
import com.example.simpledish.model.Dish
import com.example.simpledish.model.Ingredient
import com.example.simpledish.navigation.AddDestination
import com.example.simpledish.navigation.DetailsDestination
import com.example.simpledish.ui.screens.homeScreen.HomeScreenBody
import com.example.simpledish.ui.theme.SimpleDishTheme
import com.example.simpledish.ui.theme.darkBlueIsh

@Composable
fun DishDetailScreen(
    navigateBack: (Int) -> Unit,
    navigateToEdit: (Int) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: DetailsScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    // only when screen changes!
    LaunchedEffect(navController) {
        viewModel.reinitialize()
    }
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SimpleDishTopAppBar(
                navigateBack = { navigateBack(uiState.userId) },
                canNavigateBack = true,
                title = stringResource(DetailsDestination.titleRes)
            )
        },
        floatingActionButton = {
            if (uiState.didUserCreateDish) {
                floatingActionButton { navigateToEdit(uiState.dishId) }
            }
        },
        modifier = modifier
    ) { padding ->

        DishDetailBody(
            dish = uiState.dish,
            ingredients = uiState.ingredients,
            onAddSaveButtonClick = viewModel::onAddSaveButtonClick,
            didUserSaveDish = uiState.didUserSaveDish,
            onDeleteSaveButtonClick = viewModel::onDeleteSaveButtonClick,
            didUserCreateDish = uiState.didUserCreateDish,
            onDeleteDish = viewModel::onDeleteDishButtonClick,
            navigateBack = { navigateBack(uiState.userId) },
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun floatingActionButton(onFABClick: () -> Unit) {
    FloatingActionButton(onClick = onFABClick) {
        Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit dish")

    }
}

@Composable
fun DishDetailBody(
    dish: Dish,
    ingredients: List<Ingredient>,     // onDeleteButtonClick
    onAddSaveButtonClick: () -> Unit,
    onDeleteSaveButtonClick: () -> Unit,
    didUserSaveDish: Boolean,
    didUserCreateDish: Boolean,
    onDeleteDish: () -> Unit,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current // for the toast

    Column(modifier = modifier.padding(16.dp)) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {

                Text(
                    dish.name,
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 25.dp)
                )

                Spacer(modifier = Modifier.size(6.dp))

                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Prep time", fontSize = 20.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(dish.prepTime.toString(), fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.size(6.dp))

                Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                    Text("Servings", fontSize = 20.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(dish.servings, fontSize = 20.sp)
                }
                Spacer(modifier = Modifier.size(6.dp))

                dishIngredientDetails(ingredients)

            }
        }
        Spacer(modifier = Modifier.size(15.dp))

        if (didUserCreateDish) {
            Button(
                onClick = {
                    onDeleteDish()
                    Toast.makeText(context, "Deleted ${dish.name}", Toast.LENGTH_SHORT).show()
                    navigateBack()

                }) {
                Text(
                    "Delete",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    fontSize = 18.sp
                )
            }
        }

        OutlinedButton(onClick = {

            if (didUserSaveDish) {
                onDeleteSaveButtonClick()
                Toast.makeText(context, "Un-saved ${dish.name}", Toast.LENGTH_SHORT).show()
            } else {
                onAddSaveButtonClick()
                Toast.makeText(context, "Saved ${dish.name}", Toast.LENGTH_SHORT).show()
            }

        }) {
            val buttonText: String = if (didUserSaveDish) {
                "Un-save"
            } else {
                "Save"
            }
            Text(
                buttonText,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun dishIngredientDetails(ingredients: List<Ingredient>) {
    Surface {
        Column {
            Text(
                text = "Ingredients",
                modifier = Modifier
                    .background(color = Color.LightGray)
                    .fillMaxWidth()
                    .padding(4.dp),
                textAlign = TextAlign.Center,
                fontSize = 22.sp,
                fontWeight = FontWeight.Medium,
            )

            Column(
                modifier = Modifier
                    .background(color = darkBlueIsh)
                    .padding(start = 16.dp, end = 16.dp, top = 6.dp)
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.size(50.dp))
                ingredients.forEach {
                    Row(modifier = Modifier.padding(bottom = 34.dp)) {
                        Text(it.ingredientName, fontSize = 20.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(it.amount(), fontSize = 20.sp)
                    }
                }
                Spacer(modifier = Modifier.size(25.dp))
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun DishDetailScreenPreview() {
    SimpleDishTheme {
        DishDetailBody(
            dish2,
            didUserSaveDish = true,
            ingredients = emptyList(),
            didUserCreateDish = true,
            onAddSaveButtonClick = {},
            onDeleteDish = {},
            onDeleteSaveButtonClick = {},
            navigateBack = {}
        )
    }
}