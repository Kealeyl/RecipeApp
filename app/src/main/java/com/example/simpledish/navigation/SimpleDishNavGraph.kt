package com.example.simpledish.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.simpledish.ui.screens.addDishScreen.AddScreen
import com.example.simpledish.ui.screens.detailsScreen.DishDetailScreen
import com.example.simpledish.ui.screens.editDishScreen.EditScreen
import com.example.simpledish.ui.screens.homeScreen.HomeScreen
import com.example.simpledish.ui.screens.loginScreen.LoginScreen
import com.example.simpledish.ui.screens.searchScreen.SearchScreen

@Composable
fun SimpleDishNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {

        composable(LoginDestination.route) {
            LoginScreen(
                navigateToHomePage = {
                    navController.navigate("${HomeDestination.route}/${it}") {
                        popUpTo(LoginDestination.route) {
                            inclusive = true
                        } // cant navigate back to login screen from back button
                    }
                })
        }

        // accepts user ID, gets passed to view model by jetpack navigation
        composable(
            route = HomeDestination.routeWithArgs,
            arguments = listOf(
                navArgument(HomeDestination.userIdArg) { type = NavType.IntType }
            )) {
            HomeScreen(
                // "$route/{$dishIdArg}/{$userIdArg}"
                navigationToDishDetail = { dishId, userId ->
                    navController.navigate("${DetailsDestination.route}/${dishId}/${userId}")
                },
                logOutButton = {
                    navController.navigate(LoginDestination.route) {
                        popUpTo(HomeDestination.route) { inclusive = true }
                    }
                },
                onAddTabClick = {
                    navController.navigate("${AddDestination.route}/${it}")
                },
                onSearchTabClick = {
                    navController.navigate("${SearchDestination.route}/${it}") {
                        popUpTo(HomeDestination.route) { inclusive = true }
                    }
                },
                navController = navController
            )
        }

        // needs userId
        // navigate back on successful submit
        composable(route = AddDestination.routeWithArgs,
            arguments = listOf(
                navArgument(AddDestination.userIdArg) { type = NavType.IntType }
            )) {
            AddScreen(
                onSearchTabClick = {
                    navController.navigate("${SearchDestination.route}/${it}") {
                        popUpTo(AddDestination.route) { inclusive = true }
                    }
                },
                onHomeTabClick = {
                    navController.navigate("${HomeDestination.route}/${it}") {
                        popUpTo(SearchDestination.route) { inclusive = true }
                    }
                },
            )
        }

        composable(
            route = DetailsDestination.routeWithArgs,
            arguments = listOf(
                navArgument(DetailsDestination.dishIdArg) { type = NavType.IntType },
                navArgument(DetailsDestination.userIdArg) { type = NavType.IntType }
            )) {
            DishDetailScreen(
                navigateBack = { navController.popBackStack() }, //state?? user id state, popBackStack reuses viewmodel...
                navigateToEdit = { navController.navigate("${EditDishDestination.route}/${it}") },
                navController = navController
            )
        }

        // accepts dish ID, gets passed to view model by jetpack navigation
        composable(
            route = EditDishDestination.routeWithArgs,
            arguments = listOf(
                navArgument(EditDishDestination.dishIdArg) { type = NavType.IntType }
            )
        ) {
            EditScreen(navigateBack = { navController.popBackStack() })
        }

        // needs userId
        composable(route = SearchDestination.routeWithArgs,
            arguments = listOf(
                navArgument(AddDestination.userIdArg) { type = NavType.IntType }
            )) {

            SearchScreen(
                navigationToDishDetail = { dishId, userId ->
                    navController.navigate("${DetailsDestination.route}/${dishId}/${userId}")
                },
                onHomeTabClick = {
                    navController.navigate("${HomeDestination.route}/${it}") {
                        popUpTo(SearchDestination.route) { inclusive = true }
                    }
                },
                onAddTabClick = { navController.navigate("${AddDestination.route}/${it}") },
                navController = navController
            )
        }
    }
}