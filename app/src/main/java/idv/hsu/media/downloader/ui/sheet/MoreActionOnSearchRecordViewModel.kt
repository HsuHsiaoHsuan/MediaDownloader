package idv.hsu.media.downloader.ui.sheet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.db.relation.SearchAndInfo
import idv.hsu.media.downloader.domain.DeleteSearchRecordUseCase
import idv.hsu.media.downloader.domain.DownloadMediaUseCase
import idv.hsu.media.downloader.worker.MediaType
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MoreActionOnSearchRecordViewModel @Inject constructor(
    private val downloadMediaUseCase: DownloadMediaUseCase,
    private val delSearchRecordUseCase: DeleteSearchRecordUseCase
) : ViewModel() {

    fun downloadMedia(url: String, fileName: String, @MediaType type: Int) = viewModelScope.launch {
        downloadMediaUseCase(url, fileName, type)
    }

    fun delSearchRecord(record: SearchAndInfo) {
        viewModelScope.launch {
            delSearchRecordUseCase(record)
        }
    }
}