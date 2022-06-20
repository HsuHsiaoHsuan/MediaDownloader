package idv.hsu.media.downloader.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import idv.hsu.media.downloader.ui.theme.MyApplicationTheme

@Composable
fun SettingsScreen() {
    MyApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.background) {

        }
    }
}

@Composable
private fun myText(value: String) {
    Text(text = value)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            myText(value = "HELLO")
        }

    }
}