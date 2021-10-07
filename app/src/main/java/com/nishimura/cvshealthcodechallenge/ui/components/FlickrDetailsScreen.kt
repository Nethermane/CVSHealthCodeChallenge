package com.nishimura.cvshealthcodechallenge.ui.components

import android.widget.TextView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.size.OriginalSize
import com.nishimura.cvshealthcodechallenge.R
import com.nishimura.cvshealthcodechallenge.model.*
import com.nishimura.cvshealthcodechallenge.ui.theme.Typography

@ExperimentalCoilApi
@Composable
fun FlickrDetailsScreen(flickrItem: Item, navController: NavController) {
    val resources = LocalContext.current.resources
    Column {
        TopAppBar(
            title = { },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Filled.Close,
                        contentDescription = stringResource(id = R.string.close_button_content_description),
                        tint = Color.White,
                    )
                }
            }
        )
        Column(Modifier.verticalScroll(rememberScrollState())) {
            Image(
                painter = rememberImagePainter(
                    data = flickrItem.media.m,
                    builder = {
                        size(OriginalSize)
                        crossfade(true)
                        placeholder(R.drawable.placeholder_grey)
                    },
                ),
                contentDescription = flickrItem.tags,
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillWidth
            )
            Text(
                text = flickrItem.getTitleOrUntitled(),
                style = Typography.h6,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(
                    vertical = dimensionResource(id = R.dimen.medium_padding),
                    horizontal = dimensionResource(
                        id = R.dimen.medium_padding
                    )
                )
            )
            Text(
                text = resources.getString(
                    R.string.flickr_details_by,
                    (flickrItem.author.replace(Regex("nobody@flickr.com \\(\""), "")
                        .removeSuffix("\")"))
                ), Modifier.padding(
                    top = dimensionResource(id = R.dimen.small_padding),
                    start = dimensionResource(id = R.dimen.small_padding),
                    end = dimensionResource(id = R.dimen.small_padding),
                    bottom = dimensionResource(id = R.dimen.large_padding)
                )
            )
            flickrItem.parseDescriptionFromHtml()?.let {
                Html(
                    it,
                    modifier = Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_padding))
                )
            }
            Text(
                text = resources.getString(
                    R.string.flickr_details_width,
                    (flickrItem.parseWidthFromHtml() ?: 0)
                ),
                Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_padding))
            )
            Text(
                text = resources.getString(
                    R.string.flickr_details_height,
                    (flickrItem.parseHeightFromHtml() ?: 0)
                ),
                Modifier.padding(horizontal = dimensionResource(id = R.dimen.small_padding))
            )
            Spacer(modifier = Modifier.height(100.dp))
        }
    }
}

/**
 * Per: https://stackoverflow.com/a/68800288
 */
@Composable
fun Html(htmlText: String, modifier: Modifier = Modifier) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            text = HtmlCompat.fromHtml(htmlText, HtmlCompat.FROM_HTML_MODE_LEGACY)
        }
    }, modifier = modifier)
}