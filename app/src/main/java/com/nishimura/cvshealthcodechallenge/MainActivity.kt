package com.nishimura.cvshealthcodechallenge

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.nishimura.cvshealthcodechallenge.ui.components.FlickrDetailsScreen
import com.nishimura.cvshealthcodechallenge.ui.components.FlickrSearchScreen
import com.nishimura.cvshealthcodechallenge.ui.theme.CVSHealthCodeChallengeTheme
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModel
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModelFactory


/**
 * General feedback:
 *
 * Task 1: A list should be below the items in #2, and should be a grid with 2 columns.
 * This requirement is very vague because the acceptance criteria is a bulleted list not a numbered one
 *
 * Task 1: The title should be centered under the image.
 * I wish I was allowed to use more columns for horizontal view, it would look a lot better
 *
 * Task 1: "As the user changes the text in the search bar, update the list or grid of images."
 *    and "A search box and search button should be at the top screen."
 * These two requirements sound counter to each other. There is no point of a search button if it
 * auto searches every time the text changes. I assume the actually intended phrasing
 * of the first is "When the user changes text in the search bar and hits search, update the list or grid of images."
 *
 * Task 1: When performing the search, indicate progress while not blocking the UI.
 * This is an unclear requirement. Does it mean not blocking the UI thread or not covering existing UI
 *
 *
 */
@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<FlickrImageViewModel> {
        FlickrImageViewModelFactory(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CVSHealthCodeChallengeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainActivityScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@ExperimentalCoilApi
@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun MainActivityScreen(
    viewModel: FlickrImageViewModel,
    injectedNavController: NavHostController? = null
) {
    CVSHealthCodeChallengeTheme {
        val navController = injectedNavController ?: rememberNavController()
        NavHost(navController = navController, startDestination = "search") {
            composable("search") {
                FlickrSearchScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable(
                "details/{index}",
                arguments = listOf(navArgument("index") {
                    type = NavType.IntType
                })
            ) { _navBackStack ->
                _navBackStack.arguments?.getInt("index")?.let { viewModel.getItemByIndex(it) }
                    ?.let {
                        FlickrDetailsScreen(
                            flickrItem = it,
                            navController = navController
                        )
                    }

            }
        }
    }
}