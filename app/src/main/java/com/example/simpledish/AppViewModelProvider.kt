package com.example.simpledish

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.simpledish.ui.screens.addDishScreen.AddDishScreenViewModel
import com.example.simpledish.ui.screens.detailsScreen.DetailsScreenViewModel
import com.example.simpledish.ui.screens.editDishScreen.EditDishScreenViewModel
import com.example.simpledish.ui.screens.homeScreen.HomeScreenViewModel
import com.example.simpledish.ui.screens.loginScreen.LoginScreenViewModel
import com.example.simpledish.ui.screens.searchScreen.SearchScreenViewModel

// Singleton
object AppViewModelProvider {

    val Factory = viewModelFactory {
        // SavedStateHandle contains the detailId passed via navigation, done automatically by Jetpack Navigation

        // for AddDishScreenViewModel
        initializer {
            AddDishScreenViewModel(
                this.createSavedStateHandle(),
                simpleDishApplication().container.simpleDishDatabaseRepository)
        }

        // for DetailsScreenViewModel
        initializer {
            DetailsScreenViewModel(
                this.createSavedStateHandle(), // Create and inject a SavedStateHandle
                simpleDishApplication().container.simpleDishDatabaseRepository
            )
        }

        // for EditDishScreenViewModel
        initializer {
            EditDishScreenViewModel(
                this.createSavedStateHandle(),
                simpleDishApplication().container.simpleDishDatabaseRepository
            )
        }

        // for HomeScreenViewModel
        initializer {
            HomeScreenViewModel(
                this.createSavedStateHandle(),
                simpleDishApplication().container.simpleDishDatabaseRepository
            )
        }

        // for LoginScreenViewModel
        initializer {
            LoginScreenViewModel(simpleDishApplication().container.simpleDishDatabaseRepository)
        }

        // for SearchScreenViewModel
        initializer {
            SearchScreenViewModel(
                this.createSavedStateHandle(),
                simpleDishApplication().container.simpleDishDatabaseRepository)
        }
    }
}

// used to find the app's SimpleDishAppApplication object
// extension function
fun CreationExtras.simpleDishApplication(): SimpleDishAppApplication {
    return (this[AndroidViewModelFactory.APPLICATION_KEY] as SimpleDishAppApplication)
}