package idv.hsu.media.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.repository.YtDlpRepository
import idv.hsu.media.downloader.worker.MediaType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GetMediaViewModel @Inject constructor(
    private val repoYtdlp: YtDlpRepository
) : ViewModel() {

//    private val _uiState = MutableSharedFlow<DownloadMediaUiState>()
//    val uiState: SharedFlow<DownloadMediaUiState> = _uiState

    fun downloadMedia(url: String, fileName: String, @MediaType type: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repoYtdlp.getMedia(url, fileName, type)
        }
    }

    fun cancelDownload(url: String, fileName: String, @MediaType type: Int) =
        viewModelScope.launch(Dispatchers.IO) {
            repoYtdlp.cancelGetMedia(url, fileName, type)
        }
}

//sealed class DownloadMediaUiState {
//    data class DownloadingUrl(val url: String) : ParseMediaUiState()
//    data class DownloadOk(val url: String, val videoInfo: VideoInfo) : ParseMediaUiState()
//    data class DownloadFail(val url: String, val message: String) : ParseMediaUiState()
//}