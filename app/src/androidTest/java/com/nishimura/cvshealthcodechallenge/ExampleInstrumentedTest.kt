package com.nishimura.cvshealthcodechallenge

import android.app.Application
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.NavHostController
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.compose.DialogNavigator
import androidx.test.espresso.Espresso
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import coil.annotation.ExperimentalCoilApi
import com.nishimura.cvshealthcodechallenge.model.FlickrResponse
import com.nishimura.cvshealthcodechallenge.model.Item
import com.nishimura.cvshealthcodechallenge.model.Media
import com.nishimura.cvshealthcodechallenge.network.FlickrRepository
import com.nishimura.cvshealthcodechallenge.ui.theme.CVSHealthCodeChallengeTheme
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModel
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    private lateinit var navController: NavHostController

    @ExperimentalFoundationApi
    @ExperimentalCoilApi
    @ExperimentalComposeUiApi
    @get:Rule
    val composeTestRule = createComposeRule()

    val fakeRepo = object : FlickrRepository {
        override suspend fun getImages(tags: String): FlickrResponse {
            return FlickrResponse(
                "",
                "",
                listOf(Item("", "", "", "", "", Media("https://picsum.photos/200"), "", "", "")),
                "",
                "",
                ""
            )
        }
    }

    @Before
    fun before() {
        navController = NavHostController(getInstrumentation().context).apply {
            navigatorProvider.addNavigator(ComposeNavigator())
            navigatorProvider.addNavigator(DialogNavigator())
        }
    }

    @ExperimentalCoilApi
    @ExperimentalComposeUiApi
    @ExperimentalFoundationApi
    @Test
    fun testNavigationToDescriptionAndBack() {
        val fakedViewModel = FlickrImageViewModel(
            getInstrumentation().targetContext.applicationContext as Application,
            fakeRepo
        )
        composeTestRule.setContent {
            CVSHealthCodeChallengeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainActivityScreen(viewModel = fakedViewModel, navController)
                }
            }
        }
        //Start on search page
        Assert.assertEquals("search", navController.currentDestination!!.route)
        composeTestRule.onNodeWithText("Untitled").performClick()

        //Verify pressing an item navigates to details
        Assert.assertEquals("details/{index}", navController.currentDestination!!.route)

        //Verify back button takes back to search
        Espresso.pressBack()
        Assert.assertEquals("search", navController.currentDestination!!.route)

        //Verify Close button also takes user back to search
        composeTestRule.onNodeWithText("Untitled").performClick()
        Assert.assertEquals("details/{index}", navController.currentDestination!!.route)
        composeTestRule.onNodeWithContentDescription("Close button").performClick()
        Assert.assertEquals("search", navController.currentDestination!!.route)


    }

}