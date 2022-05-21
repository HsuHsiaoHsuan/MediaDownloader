package idv.hsu.media.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yausername.youtubedl_android.mapper.VideoInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.repository.YtDlpRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadMediaViewModel @Inject constructor(
    private val repoYtdlp: YtDlpRepository
) : ViewModel() {

    private val _uiState = MutableSharedFlow<DownloadMediaUiState>()
    val uiState: SharedFlow<DownloadMediaUiState> = _uiState

    fun downloadMedia(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repoYtdlp.getVideo(url)
        }
    }
}

sealed class DownloadMediaUiState {
    data class DownloadingUrl(val url: String) : ParseMediaUiState()
    data class DownloadOk(val url: String, val videoInfo: VideoInfo) : ParseMediaUiState()
    data class DownloadFail(val url: String, val message: String) : ParseMediaUiState()
}