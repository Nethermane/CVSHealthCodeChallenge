package com.nishimura.cvshealthcodechallenge.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import com.nishimura.cvshealthcodechallenge.di.ActivityModule
import com.nishimura.cvshealthcodechallenge.di.DaggerActivityComponent
import com.nishimura.cvshealthcodechallenge.ui.components.MainActivityScreen
import com.nishimura.cvshealthcodechallenge.ui.theme.CVSHealthCodeChallengeTheme
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModel
import javax.inject.Inject


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
open class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var flickrViewModel: FlickrImageViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val activityComponent = DaggerActivityComponent.builder()
            .activityModule(ActivityModule(this))
            .build()
        activityComponent.inject(this)
        setContent {
            CVSHealthCodeChallengeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainActivityScreen(viewModel = flickrViewModel)
                }
            }
        }
    }
}