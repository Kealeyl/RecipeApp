package com.example.simpledish.ui.screens.loginScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.simpledish.AppViewModelProvider
import com.example.simpledish.SimpleDishTopAppBar
import com.example.simpledish.navigation.AddDestination
import com.example.simpledish.navigation.LoginDestination
import com.example.simpledish.ui.screens.homeScreen.HomeScreenBody
import com.example.simpledish.ui.screens.homeScreen.HomeScreenUIState
import com.example.simpledish.ui.screens.homeScreen.HomeScreenViewModel
import com.example.simpledish.ui.theme.SimpleDishTheme
import kotlin.math.log

@Composable
fun LoginScreen(
    navigateToHomePage: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginScreenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            SimpleDishTopAppBar(
                canNavigateBack = false,
                title = stringResource(LoginDestination.titleRes)
            )
        },
        bottomBar = {},
        snackbarHost = {},
        modifier = modifier
    ) { padding ->
        LoginBody(
            uiState = uiState,
            onValueChange = viewModel::updateUiState,
            loginButtonClick = {
                viewModel.getUserId {
                    if (it != null){
                        navigateToHomePage(it)
                    }
                }
            }, //Ok.. validation ?
            modifier = Modifier.padding(padding)
        )
    }
}

@Composable
fun LoginBody(
    uiState: LoginScreenUIState,
    onValueChange: (LoginScreenUIState) -> Unit,
    loginButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {

        Card {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = uiState.username,
                    onValueChange = { onValueChange(uiState.copy(username = it)) },
                    label = { Text("Username") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    isError = uiState.isWrongUserNameOrPassword,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = uiState.password,
                    onValueChange = { onValueChange(uiState.copy(password = it)) },
                    label = { Text("Password") },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { loginButtonClick() }
                    ),
                    isError = uiState.isWrongUserNameOrPassword,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = loginButtonClick,
                    enabled = uiState.isValidInput,
                    modifier = Modifier.fillMaxWidth()
                ) { Text("Login") }
            }
        }
        Spacer(modifier = Modifier.size(80.dp))
    }
}

@Preview(showSystemUi = true)
@Composable
fun LoginPreview() {
    SimpleDishTheme {
        LoginBody(loginButtonClick = {}, onValueChange = {}, uiState = LoginScreenUIState())
    }
}