package com.example.simpledish.navigation

import com.example.simpledish.R
import com.example.simpledish.navigation.EditDishDestination.dishIdArg

interface NavigationDestination {
    val route: String
    val titleRes: Int
}

object AddDestination : NavigationDestination {
    override val route = "add"
    override val titleRes = R.string.add
    const val userIdArg = "userId"
    val routeWithArgs = "${route}/{$userIdArg}"
}

object DetailsDestination : NavigationDestination {
    override val route = "details"
    override val titleRes = R.string.dish_details_title
    const val dishIdArg = "dishId" // the name of the argument
    const val userIdArg = "userId" // the name of the argument
    val routeWithArgs = "$route/{$dishIdArg}/{$userIdArg}"
}

object EditDishDestination : NavigationDestination {
    override val route = "edit_dish"
    override val titleRes = R.string.edit_dish_title
    const val dishIdArg = "dishId"
    val routeWithArgs = "$route/{$dishIdArg}"
}

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
    const val userIdArg = "userId"
    val routeWithArgs = "${route}/{$userIdArg}"
}

object LoginDestination : NavigationDestination {
    override val route = "login"
    override val titleRes = R.string.login_title
}

object SearchDestination : NavigationDestination {
    override val route = "search"
    override val titleRes = R.string.search
    const val userIdArg = "userId"
    val routeWithArgs = "${route}/{$userIdArg}"
}

fun getTitleResForRoute(route: String): Int {
    return when (route) {
        AddDestination.routeWithArgs -> AddDestination.titleRes
        DetailsDestination.routeWithArgs -> DetailsDestination.titleRes
        EditDishDestination.routeWithArgs -> EditDishDestination.titleRes
        HomeDestination.routeWithArgs -> HomeDestination.titleRes
        LoginDestination.route -> LoginDestination.titleRes
        SearchDestination.routeWithArgs -> SearchDestination.titleRes
        else -> throw IllegalArgumentException("Route $route not found")
    }
}