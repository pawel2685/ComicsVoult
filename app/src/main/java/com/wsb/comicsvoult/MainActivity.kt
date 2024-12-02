package com.wsb.comicsvoult

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.wsb.comicsvoult.ui.theme.ComicsVoultTheme
import com.wsb.comicsvoult.view.CollectionScreen
import com.wsb.comicsvoult.view.LibraryScreen

sealed class Destination(val route: String) {
    object Library : Destination("library")
    object Collection : Destination("collection")
    object CharacterDetail : Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComicsVoultTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    CharactersScaffold(navController = navController)
                }
            }
        }
    }
}

@Composable
fun CharactersScaffold(navController: NavHostController) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            BottomAppBar {
                Text("Bottom Bar Placeholder")
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Destination.Library.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Destination.Library.route) {
                LibraryScreen()
            }
            composable(Destination.Collection.route) {
                CollectionScreen()
            }
            composable(
                route = Destination.CharacterDetail.route,
                arguments = listOf(navArgument("characterId") { type = NavType.StringType })
            ) { navBackStackEntry ->
                val characterId = navBackStackEntry.arguments?.getString("characterId")
                CharacterDetailScreen(characterId = characterId)
            }
        }
    }
}

@Composable
fun CharacterDetailScreen(characterId: String?) {
    Text(text = "Character ID: $characterId")
}
