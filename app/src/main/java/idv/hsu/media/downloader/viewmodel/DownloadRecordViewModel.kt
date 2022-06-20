package idv.hsu.media.downloader.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import idv.hsu.media.downloader.db.relation.DownloadAndInfo
import idv.hsu.media.downloader.repository.DownloadRecordRepository
import idv.hsu.media.downloader.vo.DownloadRecord
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DownloadRecordViewModel @Inject constructor(
    private val repoDownloadRecord: DownloadRecordRepository
) : ViewModel() {

    val allDownloadRecord = repoDownloadRecord.allDownloadRecord

    val allVideoRecord = repoDownloadRecord.allVideoRecord

    val allAudioRecord = repoDownloadRecord.allAudioRecord

    fun delete(record: DownloadRecord) = viewModelScope.launch {
        repoDownloadRecord.deleteDownloadRecord(record)
    }

}

sealed class DownloadCategoryUiState {
    object NoData : DownloadCategoryUiState()
    data class AllData(val list: List<DownloadAndInfo>) : DownloadCategoryUiState()
    data class AllVideo(val list: List<DownloadAndInfo>) : DownloadCategoryUiState()
    data class AllAudio(val list: List<DownloadAndInfo>) : DownloadCategoryUiState()
}