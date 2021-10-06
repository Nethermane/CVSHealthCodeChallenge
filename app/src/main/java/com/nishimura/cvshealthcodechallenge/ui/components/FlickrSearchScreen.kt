package com.nishimura.cvshealthcodechallenge.ui.components

import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.google.gson.Gson
import com.nishimura.cvshealthcodechallenge.R
import com.nishimura.cvshealthcodechallenge.model.getTitleOrUntitled
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModel
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun FlickrSearchScreen(viewModel: FlickrImageViewModel, navController: NavController) {
    var searchValue by remember { mutableStateOf("") }
    val searchResponse = viewModel.flickrImages.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyGridState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    fun doSearch() {
        viewModel.searchForImages(searchValue)
        keyboardController?.hide()
        coroutineScope.launch {
            lazyGridState.scrollToItem(0)
        }

    }

    Column {
        TopAppBar(
            title = { Text(stringResource(id = R.string.flickr_search_title)) },
        )
        if (isLoading.value) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        } else {
            Spacer(Modifier.height(4.dp))
        }
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = searchValue,
                onValueChange = {
                    searchValue = it
                },
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        stringResource(id = R.string.search_content_description)
                    )
                },
                singleLine = true,
                label = { Text(stringResource(id = R.string.flickr_search_hint)) },
                modifier = Modifier
                    .weight(1f)
                    .padding(dimensionResource(id = R.dimen.medium_padding)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = {
                    doSearch()
                })
            )
            Button(modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(dimensionResource(id = R.dimen.small_padding)),
                onClick = {
                    doSearch()
                }) {
                Text(text = stringResource(id = R.string.search_button_text))
            }
        }
        val gridPadding = when (LocalConfiguration.current.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                PaddingValues(
                    vertical = dimensionResource(id = R.dimen.small_padding),
                    horizontal = dimensionResource(id = R.dimen.extra_extra_large_padding)
                )
            }
            else -> {
                PaddingValues(dimensionResource(id = R.dimen.small_padding))
            }
        }
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            contentPadding = gridPadding,
            state = lazyGridState
        ) {
            items(searchResponse.value?.items?.size ?: 0) {
                Card(
                    Modifier
                        .padding(PaddingValues(dimensionResource(id = R.dimen.extra_small_padding)))
                        .heightIn(max = 125.dp)
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("details/$it")
                        }
                ) {
                    Column(
                        modifier = Modifier
                            .heightIn(max = 125.dp)
                            .fillMaxWidth()
                    ) {
                        Image(
                            painter = rememberImagePainter(
                                data = searchResponse.value?.items?.get(it)?.media?.m,
                                builder = {
                                    crossfade(true)
                                    placeholder(R.drawable.placeholder_grey)
                                },
                            ),
                            contentDescription = searchResponse.value?.items?.get(it)?.tags,
                            modifier = Modifier.weight(1f).fillMaxWidth(),
                            contentScale = ContentScale.Crop
                        )
                        Text(
                            text = searchResponse.value?.items?.get(it).getTitleOrUntitled(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(colorResource(id = R.color.image_label_background))
                                .padding(dimensionResource(id = R.dimen.small_padding)),
                            textAlign = TextAlign.Center,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = colorResource(id = R.color.white)
                        )
                    }
                }
            }
        }
    }
}