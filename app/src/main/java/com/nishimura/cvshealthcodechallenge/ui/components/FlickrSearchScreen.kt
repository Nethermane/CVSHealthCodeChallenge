package com.nishimura.cvshealthcodechallenge.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.nishimura.cvshealthcodechallenge.R
import com.nishimura.cvshealthcodechallenge.model.getTitleOrUntitled
import com.nishimura.cvshealthcodechallenge.ui.theme.Typography
import com.nishimura.cvshealthcodechallenge.viewmodel.FlickrImageViewModel
import kotlinx.coroutines.launch


@ExperimentalComposeUiApi
@ExperimentalCoilApi
@ExperimentalFoundationApi
@Composable
fun FlickrSearchScreen(viewModel: FlickrImageViewModel, navController: NavController) {
    val searchValue = viewModel.currentSearch.collectAsState()
    //Var to allow tapping outside of the popup to close it. Otherwise no way for horizontal to not
    //be partially obscured
    var emptyTextTapOutsideBox by remember { mutableStateOf(true) }
    val focusManager = LocalFocusManager.current
    val searchResponse = viewModel.flickrImages.collectAsState()
    val isLoading = viewModel.isLoading.collectAsState()
    val savedSearches = viewModel.savedSearches.collectAsState()
    val keyboardController = LocalSoftwareKeyboardController.current
    val lazyGridState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    viewModel.getSaveSearches()

    fun doSearch() {
        viewModel.searchForImages()
        keyboardController?.hide()
        coroutineScope.launch {
            lazyGridState.scrollToItem(0)
        }
    }

    Column {
        if (isLoading.value) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
            )
        } else {
            Spacer(Modifier.height(4.dp))
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            Column(Modifier.weight(1f)) {
                OutlinedTextField(
                    value = searchValue.value,
                    onValueChange = {
                        viewModel.setCurrentSearch(it)
                        if (it.isEmpty()) {
                            emptyTextTapOutsideBox = false
                        }
                    },
                    singleLine = true,
                    label = { Text(stringResource(id = R.string.flickr_search_hint)) },
                    modifier = Modifier
                        .padding(dimensionResource(id = R.dimen.medium_padding))
                        .fillMaxWidth()
                        .onFocusChanged {
                            if (it.isFocused && searchValue.value.isEmpty()) {
                                emptyTextTapOutsideBox = false
                            }
                        },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = {
                        doSearch()
                    })
                )
                DropdownMenu(
                    expanded =  searchValue.value.isEmpty() && !emptyTextTapOutsideBox,
                    onDismissRequest = { emptyTextTapOutsideBox = true },
                    // This line here will accomplish what you want
                    properties = PopupProperties(focusable = false),
                    modifier = Modifier.fillMaxWidth(0.75f)

                ) {
                    if(savedSearches.value.isNotEmpty()) {
                        Text(
                            text = "Recent Searches",
                            fontWeight = FontWeight.Bold,
                            style = Typography.caption,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.medium_padding))
                        )
                    } else {
                        Text(
                            text = "You have no recent searches. Enter a search term above.",
                            fontWeight = FontWeight.Bold,
                            style = Typography.caption,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(start = dimensionResource(id = R.dimen.medium_padding))
                        )
                    }
                    savedSearches.value.forEach { label ->
                        DropdownMenuItem(onClick = {
                            viewModel.setCurrentSearch(label)
                            doSearch()
                        }) {
                            Text(
                                text = label,
                                style = Typography.caption,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                    }
                }
            }
            Button(modifier = Modifier
                .align(Alignment.CenterVertically)
                .padding(dimensionResource(id = R.dimen.small_padding))
                .fillMaxHeight(),
                onClick = {
                    focusManager.clearFocus()
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
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
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