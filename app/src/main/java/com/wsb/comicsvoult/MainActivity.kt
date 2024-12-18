package com.wsb.comicsvoult

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint

import com.wsb.comicsvoult.ui.theme.ComicsVoultTheme
import com.wsb.comicsvoult.view.CharacterDetailScreen
import com.wsb.comicsvoult.view.CharactersBottomNav
import com.wsb.comicsvoult.view.CollectionScreen
import com.wsb.comicsvoult.view.LibraryScreen
import com.wsb.comicsvoult.viewmodel.CollectionDbViewModel
import com.wsb.comicsvoult.viewmodel.LibraryApiViewModel
import java.math.BigInteger
import java.security.MessageDigest

sealed class Destination(val route: String) {
    object Library : Destination("library")
    object Collection : Destination("collection")
    object CharacterDetail : Destination("character/{characterId}") {
        fun createRoute(characterId: Int?) = "character/$characterId"
    }
}

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val lvm by viewModels<LibraryApiViewModel>()
    private val cvm by viewModels<CollectionDbViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ComicsVoultTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    CharactersScaffold(navController = navController, lvm, cvm)
                }
            }
        }
        logMarvelApiDetails()
    }
    // Function to generate MD5 hash and log the full URL
    private fun logMarvelApiDetails() {
        // Example usage (replace with your actual Marvel API keys)
        val ts = System.currentTimeMillis().toString()  // Generate timestamp
        val apiSecret = BuildConfig.MARVEL_SECRET        // Replace with your private key
        val apiKey = BuildConfig.MARVEL_KEY             // Replace with your public key

        // Generate the hash
        val hash = getHash(ts, apiSecret, apiKey)

        // Construct the full URL for the API request
        val url = "http://gateway.marvel.com/v1/public/characters" +
                "?ts=$ts" +
                "&apikey=$apiKey" +
                "&hash=$hash" +
                "&nameStartsWith=Hulk"  // Example query, change it as needed

        // Log the URL to Logcat
        Log.d("MarvelAPI", "Generated URL: $url")
    }

    // Function to generate the MD5 hash
    private fun getHash(timestamp: String, privateKey: String, publicKey: String): String {
        val hashStr = timestamp + privateKey + publicKey
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(hashStr.toByteArray()))
            .toString(16)
            .padStart(32, '0')
    }
}


@Composable
fun CharactersScaffold(navController: NavHostController, lvm: LibraryApiViewModel, cvm: CollectionDbViewModel) {
    val scaffoldState = rememberScaffoldState()
    val ctx = LocalContext.current

    Scaffold(
        scaffoldState = scaffoldState,
        bottomBar = {
            BottomAppBar {
                CharactersBottomNav(navController = navController)
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = Destination.Library.route,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable(Destination.Library.route) {
                LibraryScreen(navController, lvm, paddingValues)
            }
            composable(Destination.Collection.route) {
                CollectionScreen()
            }
            composable(
                route = Destination.CharacterDetail.route,
                arguments = listOf(navArgument("characterId") { type = NavType.StringType })
            ) { navBackStackEntry ->
                val id = navBackStackEntry.arguments?.getString("characterId")?.toIntOrNull()
                if(id == null)
                    Toast.makeText(ctx, "Character id is required", Toast.LENGTH_SHORT).show()
                else {
                    lvm.retrieveSingleCharacter(id)
                    CharacterDetailScreen(
                        lvm = lvm,
                        cvm = cvm,
                        paddingValues = paddingValues,
                        navController = navController
                    )
                }
            }
        }
    }
}

