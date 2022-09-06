package idv.hsu.media.downloader.ui.download

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.domain.*
import idv.hsu.media.downloader.vo.DownloadRecord
import idv.hsu.media.downloader.vo.SEARCH_TYPE_WEBVIEW
import idv.hsu.media.downloader.worker.MediaType
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadCategoryViewModel @Inject constructor(
    private val getMediaInfoUseCase: GetMediaInfoUseCase,
    private val getDownloadRecordUseCase: GetDownloadRecordUseCase,
    private val delDownloadRecordUseCase: DeleteDownloadRecordUseCase,
    private val downloadMediaUseCase: DownloadMediaUseCase,
    private val pauseDownloadUseCase: PauseDownloadUseCase,
    private val cancelDownloadUseCase: CancelDownloadUseCase
) : ViewModel() {

    val downloadAll = getDownloadRecordUseCase.downloadAll

    val downloadAudio = getDownloadRecordUseCase.downloadAudio

    val downloadVideo = getDownloadRecordUseCase.downloadVideo

    fun getMediaInfo(url: String) = viewModelScope.launch {
        // FIXME it's not SEARCH_TYPE_WEBVIEW, just workaround
        getMediaInfoUseCase(url, SEARCH_TYPE_WEBVIEW)
//        getMediaInfoUseCase(url, SEARCH_TYPE_WEBVIEW)
    }

    fun deleteDownloadRecord(record: DownloadRecord) = viewModelScope.launch {
        delDownloadRecordUseCase(record)
    }

    fun downloadMedia(url: String, fileName: String, @MediaType type: Int) = viewModelScope.launch {
        downloadMediaUseCase(url, fileName, type)
    }

    fun pause(record: DownloadRecord) = viewModelScope.launch {
        pauseDownloadUseCase(record)
    }

    fun cancelDownload(url: String, fileName: String, @MediaType type: Int) = viewModelScope.launch {
        cancelDownloadUseCase(url, fileName, type)
    }


}