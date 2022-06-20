package idv.hsu.media.downloader.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import idv.hsu.media.downloader.viewmodel.DownloadRecordViewModel

@Composable
fun DownloadScreen() {
    DownloadContent()
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun DownloadContent() {
    androidx.compose.material3.Surface(color = MaterialTheme.colorScheme.background) {
        HorizontalPager(count = 2) { page ->

        }
    }
}

@Composable
fun DownloadCategory(downloadRecordViewModel: DownloadRecordViewModel = viewModel()) {
    val data = downloadRecordViewModel.allDownloadRecord
}