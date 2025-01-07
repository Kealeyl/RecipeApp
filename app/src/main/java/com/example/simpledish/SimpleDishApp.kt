package com.example.simpledish

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.simpledish.navigation.AddDestination
import com.example.simpledish.navigation.HomeDestination
import com.example.simpledish.navigation.SearchDestination
import com.example.simpledish.navigation.SimpleDishNavHost

@Composable
fun SimpleDishApp(navController: NavHostController = rememberNavController()) {
    SimpleDishNavHost(navController)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDishTopAppBar(
    title: String,
    canNavigateBack: Boolean,
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back button"
                    )
                }
            }
        }
    )
}

@Composable
fun SimpleDishBottomAppBar(
    currScreen: String,
    modifier: Modifier = Modifier,
    onHomeTabClick: () -> Unit,
    onAddTabClick: () -> Unit,
    onSearchTabClick: () -> Unit
) {
    NavigationBar(modifier = modifier) {
        NavigationBarItem(
            selected = currScreen == HomeDestination.route,
            onClick = onHomeTabClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home screen"
                )
            }
        )

        NavigationBarItem(
            selected = currScreen == AddDestination.route,
            onClick = onAddTabClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add dish screen"
                )
            }
        )

        NavigationBarItem(
            selected = currScreen == SearchDestination.route,
            onClick = onSearchTabClick,
            icon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search for dish"
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDishTopAppBarHome(
    title: String,
    modifier: Modifier = Modifier,
    logOut: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(title) },
        modifier = modifier,
        actions = {
            Button(onClick = logOut) {
                Text("Log out")
            }
        }
    )
}

@Composable
fun SearchBar(
    searchValue: String,
    onSearchValueChange: (String) -> Unit,
    onSearchEnter: (String) -> Unit,
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
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = { onSearchEnter(searchValue) },
            onDone = { onSearchEnter(searchValue) }
        ),
        modifier = modifier
    )
}