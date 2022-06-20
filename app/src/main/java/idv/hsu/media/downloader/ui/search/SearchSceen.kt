package idv.hsu.media.downloader.ui.search

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import idv.hsu.media.downloader.R

@Preview()
@Composable
fun SearchIcon(onClickAction: () -> Unit) {
    MaterialTheme {
        IconButton(
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                enabled = true,
                indication = rememberRipple(bounded = true),
                onClick = { }
            ), onClick = { onClickAction.invoke() }) {
            Icon(
                painter = painterResource(id = R.drawable.ic_search_24),
                tint = MaterialTheme.colorScheme.onPrimary,
                contentDescription = null
            )
        }
    }
}