package com.nishimura.cvshealthcodechallenge.ui.components

import android.annotation.SuppressLint
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nishimura.cvshealthcodechallenge.R
import com.nishimura.cvshealthcodechallenge.ui.theme.CVSHealthCodeChallengeTheme
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModel

@Composable
fun MainActivityScreen(
    viewModel: FlickrImageViewModel,
) {
    _MainActivityScreen(viewModel, null)
}

/**
 * I want to only expose the composable through the main [MainActivityScreen] and hide this private
 * real implementation.
 * Suppressing this since it's most clear to name the private backing compose view as close to the
 * same and this follows naming conventions Kotlin uses
 */
@SuppressLint("ComposableNaming")
@Composable
private fun _MainActivityScreen(
    viewModel: FlickrImageViewModel,
    injectedNavController: NavHostController? = null
) {
    val resources = LocalContext.current.resources
    CVSHealthCodeChallengeTheme {
        val navController = injectedNavController ?: rememberNavController()
        NavHost(
            navController = navController,
            startDestination = resources.getString(R.string.search_route)
        ) {
            composable(resources.getString(R.string.search_route)) {
                FlickrSearchScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
            composable(
                resources.getString(R.string.details_route, "{index}"),
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

/**
 * This essentially only exists to inject a nav controller in so I can test navigation
 */
@VisibleForTesting
@Composable
fun MainActivityScreen(
    viewModel: FlickrImageViewModel,
    injectedNavController: NavHostController
) {
    _MainActivityScreen(viewModel, injectedNavController)
}